package com.cotato.squadus.domain.club.match.service;

import com.cotato.squadus.api.match.dto.matchPost.request.*;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponse;
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
import com.cotato.squadus.domain.club.match.repository.MatchPostRepository;
import com.cotato.squadus.domain.club.match.repository.MatchRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);
    private final MatchPostRepository matchPostRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;

    @Transactional
    public MatchCreateResponse createMatch(MatchCreateRequest matchCreateRequest) {
        Club homeClub = clubRepository.findById(matchCreateRequest.getHomeClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        // 해당 club의 임원인지 확인
        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), matchCreateRequest.getClubMemberId()).isPresent()) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        MatchPlace matchPlace = new MatchPlace(matchCreateRequest.getMatchPlace().getCity(),
                matchCreateRequest.getMatchPlace().getDistrict());

        MatchPost matchPost = MatchPost.builder()
                .homeClub(homeClub)
                .title(matchCreateRequest.getTitle())
                .content(matchCreateRequest.getContent())
                .tier(Tier.valueOf(matchCreateRequest.getTier()))
                .matchPlace(matchPlace)
                .placeProvided(matchCreateRequest.getPlaceProvided())
                .matchStartDate(matchCreateRequest.getMatchStartDate())
                .matchStartTime(matchCreateRequest.getMatchStartTime())
                .maxParticipants(matchCreateRequest.getMaxParticipants())
                .build();

        homeClub.addMatchPost(matchPost);

        matchPostRepository.save(matchPost);
        return MatchCreateResponse.from(matchPost);
    }


    public List<MatchCreateResponse> findAllMatches(Long memberId) {

        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(memberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        Club userClub = clubMember.getClub();
        LocalDateTime now = LocalDateTime.now();

        return matchPostRepository.findAll().stream()
                .filter(matchPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(matchPost.getMatchStartDate(), matchPost.getMatchStartTime());
                    return !matchPost.getHomeClub().equals(userClub) && matchDateTime.isAfter(now);
                })
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    public Page<MatchCreateResponse> findAllMatches(Long memberId, Pageable pageable) {
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(memberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        Club userClub = clubMember.getClub();
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        // 데이터베이스에서 직접 필터링 및 페이징 처리하여 가져오기
        Page<MatchPost> matchPostPage = matchPostRepository
                .findAllByHomeClubNotAndMatchStartDateAfterOrMatchStartDateEqualsAndMatchStartTimeAfter(
                        userClub, today, nowTime, pageable);

        // 페이지 내용을 DTO로 변환
        return matchPostPage.map(MatchCreateResponse::from);
    }


    public List<MatchCreateResponse> getFilteredMatches(FilterRequest filterRequest, Long clubMemberId) {

        // 사용자의 ClubMember 정보를 가져옵니다.
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(clubMemberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 사용자의 동아리를 가져옵니다.
        Club userClub = clubMember.getClub();

        SportsCategory sportsCategory = null;
        ClubTier tier = null;
        LocalDateTime now = LocalDateTime.now();

        if (filterRequest.getSportsCategory() != null) {
            sportsCategory = SportsCategory.valueOf(filterRequest.getSportsCategory());
        }

        if (filterRequest.getTier() != null) {
            tier = ClubTier.valueOf(filterRequest.getTier());
        }

        List<MatchPost> matchPosts = matchPostRepository.customFindMatchesByFilter(
                sportsCategory,
                filterRequest.getCity(),
                filterRequest.getDistrict(),
                tier,
                filterRequest.getPlaceProvided()
        );

        return matchPosts.stream()
                .filter(matchPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(matchPost.getMatchStartDate(), matchPost.getMatchStartTime());
                    return !matchPost.getHomeClub().equals(userClub) && matchDateTime.isAfter(now);
                })
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    public List<MatchCreateResponse> searchMatches(SearchRequest searchRequest,  Long clubMemberId) {
        // 사용자의 ClubMember 정보를 가져옵니다.
        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(clubMemberId)
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 사용자의 동아리를 가져옵니다.
        Club userClub = clubMember.getClub();
        LocalDateTime now = LocalDateTime.now();

        return matchPostRepository.customFindByKeyword(searchRequest.getKeyword()).stream()
                .filter(matchPost -> {
                    LocalDateTime matchDateTime = LocalDateTime.of(matchPost.getMatchStartDate(), matchPost.getMatchStartTime());
                    return !matchPost.getHomeClub().equals(userClub) && matchDateTime.isAfter(now);
                }) // 사용자의 동아리에서 작성한 글 제외
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public MatchRequestResponse sendMatchRequest(MatchRequestRequest matchRequestRequest) {
        //매칭을 요청한 ClubMember
        ClubMember clubMember = clubMemberRepository.findById(matchRequestRequest.getClubMemberId())
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        //다른 동아리에서 작성한 매칭 게시글
        MatchPost matchPost = matchPostRepository.findById(matchRequestRequest.getMatchPostId())
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // ClubMember가 속한 동아리가 MatchPost 작성 동아리인지 확인
        if (matchPost.getHomeClub().equals(clubMember.getClub())) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        // 이미 신청한 용병 요청이 있는지 확인
        if (matchRequestRepository.findTop1ByClubAndMatchPost(matchPost.getHomeClub(), matchPost).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_REQUEST);
        }


        MatchRequest matchRequest = MatchRequest.builder()
                .club(clubMember.getClub()) // 다시 보기
                .matchPost(matchPost)
                .status(MatchingStatus.PENDING)
                .build();

        matchPost.addMatchRequest(matchRequest);

        matchRequestRepository.save(matchRequest);
        return MatchRequestResponse.from(matchRequest);
    }

    @Transactional
    public MatchCreateResponse updateMatchPost(Long matchPostId, MatchCreateRequest matchCreateRequest) {
        MatchPost matchPost= matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        Club homeClub = clubRepository.findById(matchCreateRequest.getHomeClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), matchCreateRequest.getClubMemberId()).isPresent()) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        matchPost.update(
                matchCreateRequest.getTitle(),
                matchCreateRequest.getContent(),
                new MatchPlace(matchCreateRequest.getMatchPlace().getCity(), matchCreateRequest.getMatchPlace().getDistrict()),
                matchCreateRequest.getPlaceProvided(),
                matchCreateRequest.getMatchStartDate(),
                matchCreateRequest.getMatchStartTime(),
                matchCreateRequest.getMaxParticipants()
        );

        matchPostRepository.save(matchPost);
        return MatchCreateResponse.from(matchPost);
    }


    @Transactional
    public MatchCreateResponse deleteMatchPost(Long matchIdx, Long clubMemberId) {
        MatchPost matchPost = matchPostRepository.findById(matchIdx)
                .orElseThrow(() -> new EntityNotFoundException("해당 용병 매칭 게시글을 찾을 수 없습니다."));

        // 해당 게시글을 작성한 동아리를 찾습니다.
        Club homeClub = matchPost.getHomeClub();

        // 요청한 사용자가 해당 동아리의 관리자인지 확인합니다.
        boolean isAdmin = clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), clubMemberId)
                .isPresent();

        // 관리자가 아니라면 접근 권한이 없음을 예외 처리합니다.
        if (!isAdmin) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }

        matchPostRepository.delete(matchPost);
        return MatchCreateResponse.from(matchPost);
    }



}
