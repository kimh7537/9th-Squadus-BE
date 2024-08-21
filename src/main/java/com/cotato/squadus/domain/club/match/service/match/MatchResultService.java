package com.cotato.squadus.domain.club.match.service.match;

import com.cotato.squadus.api.match.dto.matchResult.request.MatchResultAddRequest;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchDetailResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchFinalResultResponse;
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

        //요청을 신청한 club
        Club awayClub = acceptedRequest.getClub();

        //매치 결과도 함께 반환
        List<MatchResultResponse> matchResults = matchResultRepository.findByMatchPost(matchPost).stream()
                .map(MatchResultResponse::from)
                .collect(Collectors.toList());

        return MatchDetailResponse.from(matchPost, awayClub, matchResults);
    }

    public MatchFinalResultResponse getWinningMatchResult(Long matchPostId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
        return new MatchFinalResultResponse(matchPost.getHomeWins(), matchPost.getAwayWins());
    }


    @Transactional
    public MatchResultResponse addMatchResult(Long matchPostId, MatchResultAddRequest matchResultAddRequest) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // Home 팀의 Admin인지 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), matchResultAddRequest.getClubMemberId())
                .orElseThrow(() ->  new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));

        MatchResult matchResult = MatchResult.builder()
                .matchPost(matchPost)
                .homeClub(matchPost.getHomeClub())
                .awayClub(acceptedRequest.getClub())
                .homeScore(matchResultAddRequest.getHomeScore())
                .awayScore(matchResultAddRequest.getAwayScore())
                .build();

        matchResultRepository.save(matchResult);
        return MatchResultResponse.from(matchResult);
    }

    @Transactional
    public MatchResultResponse updateMatchResult(Long matchResultId, MatchResultAddRequest matchResultAddRequest) {
        MatchResult matchResult = matchResultRepository.findById(matchResultId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 결과를 찾을 수 없습니다."));

        MatchPost matchPost = matchResult.getMatchPost();

        // Home 팀의 Admin인지 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), matchResultAddRequest.getClubMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        // 기존 점수를 수정
        matchResult.updateScores(matchResultAddRequest.getHomeScore(), matchResultAddRequest.getAwayScore());

        matchResultRepository.save(matchResult);
        return MatchResultResponse.from(matchResult);
    }



    @Transactional
    public MatchFinalResultResponse getFinalMatchResult(Long matchPostId, Long memberId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(matchPost.getHomeClub().getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        int homeWins = 0;
        int awayWins = 0;
        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
        for (MatchResult result : matchResults) {
            if (result.getHomeScore() > result.getAwayScore()) {
                homeWins++;
            } else if (result.getHomeScore() < result.getAwayScore()) {
                awayWins++;
            }
        }

        matchPost.updateWinCounts(homeWins, awayWins);
        return new MatchFinalResultResponse(homeWins, awayWins);
    }


    @Transactional
    public void finalizeHomeResult(Long matchPostId, Long memberId){

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

        // Away 팀의 ClubId를 가져옴
        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));

        Club awayClub = acceptedRequest.getClub();

        // Away 팀의 Admin인지 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(awayClub.getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);

        // 결과 확정
        matchResults.forEach(MatchResult::finalizeAwayResult);

        int homeWins = matchPost.getHomeWins();
        int awayWins = matchPost.getAwayWins();

        if (homeWins > awayWins) {
            matchPost.getHomeClub().updateMatchScore(7);
            awayClub.updateMatchScore(3);
        } else if (homeWins < awayWins) {
            matchPost.getHomeClub().updateMatchScore(3);
            awayClub.updateMatchScore(7);
        } else { // 비긴 경우
            matchPost.getHomeClub().updateMatchScore(5);
            awayClub.updateMatchScore(5);
        }

        matchResultRepository.saveAll(matchResults);
    }



    @Transactional
    public void rejectFinalResult(Long matchPostId, Long memberId) {
        MatchPost matchPost = matchPostRepository.findById(matchPostId)
                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));

        // Away 팀의 ClubId를 가져옴
        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));

        Club awayClub = acceptedRequest.getClub();

        // Away 팀의 Admin인지 확인
        clubAdminMemberRepository.findActiveAdminByClubIdAndClubMemberId(awayClub.getClubId(), memberId)
                .orElseThrow(() -> new AppException(ErrorCode.CLUB_ACCESS_DENIED));

        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
        matchResults.forEach(result -> {
            result.setIsFinalizedHome(false);
            result.setIsfinalizedAway(false);
        });

        matchResultRepository.saveAll(matchResults);
    }
}


