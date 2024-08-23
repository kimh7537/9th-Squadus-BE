package com.cotato.squadus.domain.club.match.service.mercenary;

import com.cotato.squadus.api.match.dto.matchPost.request.FilterRequest;
import com.cotato.squadus.api.match.dto.matchPost.request.SearchRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryCreateRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryRequestRequest;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryCreateResponse;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryRequestResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.*;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import com.cotato.squadus.domain.club.match.repository.mercenary.MercenaryPostRepository;
import com.cotato.squadus.domain.club.match.repository.mercenary.MercenaryRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final MemberRepository memberRepository;

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



    public List<MercenaryCreateResponse> findAllMatches() {

        LocalDateTime now = LocalDateTime.now();

        return mercenaryPostRepository.findAll().stream()
                .filter(mercenaryPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(mercenaryPost.getMatchStartDate(), mercenaryPost.getMatchStartTime());
                    return matchDateTime.isAfter(now);
                })
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    public Page<MercenaryCreateResponse> findAllMatches(Pageable pageable) {

        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        // 데이터베이스에서 직접 페이징 처리하여 가져오기
        Page<MercenaryPost> mercenaryPostsPage = mercenaryPostRepository
                .findAllByMatchStartDateAfterOrMatchStartDateEqualsAndMatchStartTimeAfter(
                        today, nowTime, pageable);


        // 페이지 내용을 DTO로 변환
        return mercenaryPostsPage.map(MercenaryCreateResponse::from);
    }


    public List<MercenaryCreateResponse> getFilteredMatches(FilterRequest filterRequest) {

        SportsCategory sportsCategory = null;
        ClubTier tier = null;
        LocalDateTime now = LocalDateTime.now();

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
                .filter(mercenaryPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(mercenaryPost.getMatchStartDate(), mercenaryPost.getMatchStartTime());
                    return matchDateTime.isAfter(now);
                })
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    public List<MercenaryCreateResponse> searchMatches(SearchRequest searchRequest) {

        LocalDateTime now = LocalDateTime.now();

        return mercenaryPostRepository.customFindByKeyword(searchRequest.getKeyword()).stream()
                .filter(mercenaryPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(mercenaryPost.getMatchStartDate(), mercenaryPost.getMatchStartTime());
                    return matchDateTime.isAfter(now);
                }) // 사용자의 동아리에서 작성한 글 제외
                .map(MercenaryCreateResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public MercenaryRequestResponse sendMatchRequest(MercenaryRequestRequest mercenaryRequestRequest, CustomOAuth2Member customOAuth2Member) {

        Member member = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
                .orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));

        //다른 동아리에서 작성한 매칭 게시글
        MercenaryPost mercenaryPost = mercenaryPostRepository.findById(mercenaryRequestRequest.getMercenaryPostId())
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // 이미 신청한 용병 요청이 있는지 확인
        if (mercenaryRequestRepository.findTop1ByMemberAndMercenaryPost(member, mercenaryPost).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_REQUEST);
        }

        MercenaryRequest mercenaryRequest = MercenaryRequest.builder()
                .member(member)
                .mercenaryPost(mercenaryPost)
                .status(MatchingStatus.PENDING)
                .build();

        mercenaryPost.addMercenaryRequest(mercenaryRequest);

        mercenaryRequestRepository.save(mercenaryRequest);
        return MercenaryRequestResponse.from(mercenaryRequest);
    }


    @Transactional
    public MercenaryCreateResponse updateMercenaryPost(Long mercenaryPostId, MercenaryCreateRequest mercenaryCreateRequest) {
        MercenaryPost mercenaryPost = mercenaryPostRepository.findById(mercenaryPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        Club homeClub = clubRepository.findById(mercenaryCreateRequest.getHomeClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), mercenaryCreateRequest.getClubMemberId()).isPresent()) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        mercenaryPost.update(
                mercenaryCreateRequest.getTitle(),
                mercenaryCreateRequest.getContent(),
                new MatchPlace(mercenaryCreateRequest.getMatchPlace().getCity(), mercenaryCreateRequest.getMatchPlace().getDistrict()),
                mercenaryCreateRequest.getPlaceProvided(),
                mercenaryCreateRequest.getMatchStartDate(),
                mercenaryCreateRequest.getMatchStartTime(),
                mercenaryCreateRequest.getMaxParticipants()
        );

        mercenaryPostRepository.save(mercenaryPost);
        return MercenaryCreateResponse.from(mercenaryPost);
    }


    @Transactional
    public MercenaryCreateResponse deleteMercenaryPost(Long mercenaryIdx, Long clubMemberId) {
        MercenaryPost mercenaryPost = mercenaryPostRepository.findById(mercenaryIdx)
                .orElseThrow(() -> new EntityNotFoundException("해당 용병 매칭 게시글을 찾을 수 없습니다."));

        // 해당 게시글을 작성한 동아리를 찾습니다.
        Club homeClub = mercenaryPost.getHomeClub();

        // 요청한 사용자가 해당 동아리의 관리자인지 확인합니다.
        boolean isAdmin = clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), clubMemberId)
                .isPresent();

        // 관리자가 아니라면 접근 권한이 없음을 예외 처리합니다.
        if (!isAdmin) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        mercenaryPostRepository.delete(mercenaryPost);
        return MercenaryCreateResponse.from(mercenaryPost);
    }


}
