package com.cotato.squadus.domain.club.match.service.match;

import com.cotato.squadus.api.match.dto.matchResult.response.MatchDetailResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchResultResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;
import com.cotato.squadus.domain.club.match.entity.match.MatchResult;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
import com.cotato.squadus.domain.club.match.repository.match.MatchPostRepository;
import com.cotato.squadus.domain.club.match.repository.match.MatchResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchResultService {

    private final MatchResultRepository matchResultRepository;
    private final MatchPostRepository matchPostRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;
    
    public MatchDetailResponse getMatchDetail(Long matchPostId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));

        Club awayClub = acceptedRequest.getClub();

        // 추가: 매치 결과도 함께 반환
        List<MatchResultResponse> matchResults = matchResultRepository.findByMatchPost(matchPost).stream()
                .map(MatchResultResponse::from)
                .collect(Collectors.toList());

        return MatchDetailResponse.from(matchPost, awayClub, matchResults);
    }

    @Transactional
    public MatchResultResponse addMatchResult(Long matchPostId, Long memberId, Integer homeScore, Integer awayScore) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // Home 팀의 Admin인지 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AccessDeniedException("경기 결과를 추가할 권한이 없습니다."));

        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));

        MatchResult matchResult = MatchResult.builder()
                .matchPost(matchPost)
                .homeClub(matchPost.getHomeClub())
                .awayClub(acceptedRequest.getClub())
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();

        matchResultRepository.save(matchResult);
        return MatchResultResponse.from(matchResult);
    }

    @Transactional
    public void finalizeMatch(Long matchPostId, Long memberId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
        matchResults.forEach(MatchResult::finalizeHomeResult);

        matchResultRepository.saveAll(matchResults);
    }

    @Transactional
    public void finalizeAwayResult(Long matchPostId, Long memberId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
        matchResults.forEach(MatchResult::finalizeAwayResult);

        // 점수 합산 로직 추가
        for (MatchResult result : matchResults) {
            if (result.getHomeScore() > result.getAwayScore()) {
                result.getHomeClub().updateMatchScore(7);
                result.getAwayClub().updateMatchScore(3);
            } else if (result.getHomeScore() < result.getAwayScore()) {
                result.getHomeClub().updateMatchScore(3);
                result.getAwayClub().updateMatchScore(7);
            } else {
                result.getHomeClub().updateMatchScore(5);
                result.getAwayClub().updateMatchScore(5);
            }
        }

        matchResultRepository.saveAll(matchResults);
    }

    @Transactional
    public void rejectFinalResult(Long matchPostId, Long memberId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
        matchResults.forEach(result -> {
            result.setIsFinalizedHome(false);
            result.setIsfinalizedAway(false);
        });

        matchResultRepository.saveAll(matchResults);
    }
}


