package com.cotato.squadus.domain.club.match.service;

import com.cotato.squadus.api.match.dto.matchPost.request.FilterRequest;
import com.cotato.squadus.api.match.dto.matchPost.request.SearchRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryCreateRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryRequestRequest;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryCreateResponse;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryRequestResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.*;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import com.cotato.squadus.domain.club.match.repository.MercenaryPostRepository;
import com.cotato.squadus.domain.club.match.repository.MercenaryRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MercenaryService {

    private final MercenaryPostRepository mercenaryPostRepository;
    private final MercenaryRequestRepository mercenaryRequestRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;

    @Transactional
    public MercenaryCreateResponse createMatch(MercenaryCreateRequest mercenaryCreateRequest) {
        Club homeClub = clubRepository.findById(mercenaryCreateRequest.getHomeClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        // 해당 club의 임원인지 확인
        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), mercenaryCreateRequest.getClubMemberId()).isPresent()) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        MatchPlace matchPlace = new MatchPlace(mercenaryCreateRequest.getMatchPlace().getCity(),
                mercenaryCreateRequest.getMatchPlace().getDistrict());

        MercenaryPost mercenaryPost = MercenaryPost.builder()
                .homeClub(homeClub)
                .title(mercenaryCreateRequest.getTitle())
                .content(mercenaryCreateRequest.getContent())
                .matchPlace(matchPlace)
                .placeProvided(mercenaryCreateRequest.getPlaceProvided())
                .matchStartDate(mercenaryCreateRequest.getMatchStartDate())
                .matchStartTime(mercenaryCreateRequest.getMatchStartTime())
                .maxParticipants(mercenaryCreateRequest.getMaxParticipants())
                .build();

        homeClub.addMercenaryPost(mercenaryPost);

        mercenaryPostRepository.save(mercenaryPost);
        return MercenaryCreateResponse.from(mercenaryPost);
    }



    public List<MercenaryCreateResponse> findAllMatches(Long memberId) {

        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(memberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        Club userClub = clubMember.getClub();

        return mercenaryPostRepository.findAll().stream()
                .filter(mercenaryPost -> !mercenaryPost.getHomeClub().equals(userClub))
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    public Page<MercenaryCreateResponse> findAllMatches(Long memberId, Pageable pageable) {
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(memberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        Club userClub = clubMember.getClub();

        // 데이터베이스에서 직접 페이징 처리하여 가져오기
        Page<MercenaryPost> mercenaryPostsPage = mercenaryPostRepository.findAllByHomeClubNot(userClub, pageable);

        // 페이지 내용을 DTO로 변환
        return mercenaryPostsPage.map(MercenaryCreateResponse::from);
    }


    public List<MercenaryCreateResponse> getFilteredMatches(FilterRequest filterRequest, Long clubMemberId) {

        // 사용자의 ClubMember 정보를 가져옵니다.
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(clubMemberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 사용자의 동아리를 가져옵니다.
        Club userClub = clubMember.getClub();

        SportsCategory sportsCategory = null;
        ClubTier tier = null;

        if (filterRequest.getSportsCategory() != null) {
            sportsCategory = SportsCategory.valueOf(filterRequest.getSportsCategory());
        }

        if (filterRequest.getTier() != null) {
            tier = ClubTier.valueOf(filterRequest.getTier());
        }

        List<MercenaryPost> mercenaryPosts = mercenaryPostRepository.customFindMatchesByFilter(
                sportsCategory,
                filterRequest.getCity(),
                filterRequest.getDistrict(),
                tier,
                filterRequest.getPlaceProvided()
        );

        return mercenaryPosts.stream()
                .filter(mercenaryPost -> !mercenaryPost.getHomeClub().equals(userClub))
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    public List<MercenaryCreateResponse> searchMatches(SearchRequest searchRequest, Long clubMemberId) {

        // 사용자의 ClubMember 정보를 가져옵니다.
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(clubMemberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 사용자의 동아리를 가져옵니다.
        Club userClub = clubMember.getClub();

        return mercenaryPostRepository.customFindByKeyword(searchRequest.getKeyword()).stream()
                .filter(mercenaryPost -> !mercenaryPost.getHomeClub().equals(userClub)) // 사용자의 동아리에서 작성한 글 제외
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public MercenaryRequestResponse sendMatchRequest(MercenaryRequestRequest mercenaryRequestRequest) {

        //매칭을 요청한 ClubMember
        ClubMember clubMember = clubMemberRepository.findById(mercenaryRequestRequest.getClubMemberId())
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        //다른 동아리에서 작성한 매칭 게시글
        MercenaryPost mercenaryPost = mercenaryPostRepository.findById(mercenaryRequestRequest.getMercenaryPostId())
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // ClubMember가 속한 동아리가 mercenaryPost의 작성 동아리인지 확인
        if (mercenaryPost.getHomeClub().equals(clubMember.getClub())) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        // 이미 신청한 용병 요청이 있는지 확인
        if (mercenaryRequestRepository.findTop1ByClubMemberAndMercenaryPost(clubMember, mercenaryPost).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_REQUEST);
        }


        MercenaryRequest mercenaryRequest = MercenaryRequest.builder()
                .clubMember(clubMember)
                .mercenaryPost(mercenaryPost)
                .status(MatchingStatus.PENDING)
                .build();

        mercenaryPost.addMercenaryRequest(mercenaryRequest);

        mercenaryRequestRepository.save(mercenaryRequest);
        return MercenaryRequestResponse.from(mercenaryRequest);
    }

}
