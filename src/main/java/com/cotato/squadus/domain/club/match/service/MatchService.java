package com.cotato.squadus.domain.club.match.service;

import com.cotato.squadus.api.match.dto.matchPost.request.*;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponse;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.match.entity.MatchPlace;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import com.cotato.squadus.domain.club.match.repository.MatchPostRepository;
import com.cotato.squadus.domain.club.match.repository.MatchRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchPostRepository matchPostRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;

    @Transactional
    public MatchCreateResponse createMatch(MatchCreateRequest matchCreateRequest) {
        Club homeClub = clubRepository.findById(matchCreateRequest.getHomeClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(matchCreateRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 해당 club의 임원인지 확인
        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(homeClub.getClubId(), matchCreateRequest.getMemberId()).isPresent()) {
            throw new AccessDeniedException("매칭 요청을 보낼 권한이 없습니다.");
        }

        MatchPlace matchPlace = new MatchPlace(matchCreateRequest.getMatchPlace().getCity(),
                matchCreateRequest.getMatchPlace().getDistrict());

        MatchPost matchPost = MatchPost.builder()
                .homeClub(homeClub)
                .sportsCategory(matchCreateRequest.getSportsCategory())
                .title(matchCreateRequest.getTitle())
                .content(matchCreateRequest.getContent())
                .tier(matchCreateRequest.getTier())
                .matchPlace(matchPlace)
                .placeProvided(matchCreateRequest.getPlaceProvided())
                .matchStartDate(matchCreateRequest.getMatchStartDate())
                .matchStartTime(matchCreateRequest.getMatchStartTime())
                .currentParticipants(matchCreateRequest.getCurrentParticipants())
                .maxParticipants(matchCreateRequest.getMaxParticipants())
                .build();

        homeClub.addMatchPost(matchPost);

        matchPostRepository.save(matchPost);
        return MatchCreateResponse.from(matchPost);
    }


    public List<MatchCreateResponse> findAllMatches() {
        return matchPostRepository.findAll().stream()
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    public Page<MatchCreateResponse> findAllMatches(Pageable pageable) {
        return matchPostRepository.findAllBy(pageable)
                .map(MatchCreateResponse::from);
    }


    public List<MatchCreateResponse> getFilteredMatches(FilterRequest filterRequest) {
        List<MatchPost> matchPosts = matchPostRepository.customFindMatchesByFilter(
                SportsCategory.valueOf(filterRequest.getSportsCategory()),
                filterRequest.getCity(),
                filterRequest.getDistrict(),
                Tier.valueOf(filterRequest.getTier()),
                filterRequest.getPlaceProvided()
        );

        return matchPosts.stream()
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    public List<MatchCreateResponse> searchMatches(SearchRequest searchRequest) {
        List<MatchPost> matchPosts = matchPostRepository.customFindByKeyword(searchRequest.getKeyword());

        return matchPosts.stream()
                .map(MatchCreateResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public MatchRequestResponse sendMatchRequest(MatchRequestRequest matchRequestRequest) {
        //매칭을 요청한 동아리
        Club club = clubRepository.findById(matchRequestRequest.getClubId())
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        //다른 동아리에서 작성한 매칭 게시글
        MatchPost matchPost = matchPostRepository.findById(matchRequestRequest.getMatchPostId())
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        ClubMember clubMember = clubMemberRepository.findClubMemberByClubMemberIdx(matchRequestRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("동아리 멤버를 찾을 수 없습니다."));

        // 해당 club의 임원인지 확인
        if (!clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(club.getClubId(), matchRequestRequest.getMemberId()).isPresent()) {
            throw new AccessDeniedException("매칭 요청을 보낼 권한이 없습니다.");
        }

        MatchRequest matchRequest = MatchRequest.builder()
                .club(club)
                .matchPost(matchPost)
                .status(MatchingStatus.PENDING)
                .isConfirmedByHomeTeam(false)
                .isConfirmedByAwayTeam(false)
                .build();

        matchPost.addMatchRequest(matchRequest);

        matchRequestRepository.save(matchRequest);
        return MatchRequestResponse.from(matchRequest);
    }

}
