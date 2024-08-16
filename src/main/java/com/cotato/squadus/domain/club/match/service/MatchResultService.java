//package com.cotato.squadus.domain.club.match.service;
//
//import com.cotato.squadus.api.match.dto.match.response.MatchDetailResponse;
//import com.cotato.squadus.domain.club.common.entity.Club;
//import com.cotato.squadus.domain.club.common.entity.ClubMember;
//import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
//import com.cotato.squadus.domain.club.match.entity.MatchPost;
//import com.cotato.squadus.domain.club.match.entity.MatchRequest;
//import com.cotato.squadus.domain.club.match.entity.MatchResult;
//import com.cotato.squadus.domain.club.match.enums.MatchingStatus;
//import com.cotato.squadus.domain.club.match.repository.MatchPostRepository;
//import com.cotato.squadus.domain.club.match.repository.MatchResultRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MatchResultService {
//
//    private final MatchResultRepository matchResultRepository;
//    private final MatchPostRepository matchPostRepository;
//    private final ClubAdminMemberRepository clubAdminMemberRepository;
//
//    @Transactional(readOnly = true)
//    public MatchDetailResponse getMatchDetail(Long matchPostId) {
//        MatchPost matchPost = matchPostRepository.findById(matchPostId)
//                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
//
//        // 승인된 MatchRequest를 찾아서 awayClub으로 설정
//        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
//                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));
//
//        Club awayClub = acceptedRequest.getClub();
//
//        return MatchDetailResponse.from(matchPost, awayClub);
//    }
//
//
//    @Transactional
//    public MatchResult addMatchResult(Long matchPostId, Long memberId, Integer homeScore, Integer awayScore) {
//        MatchPost matchPost = matchPostRepository.findById(matchPostId)
//                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
//
//        // 홈 클럽의 임원인지 확인
//        clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
//                        matchPost.getHomeClub().getClubId(), memberId)
//                .orElseThrow(() -> new AccessDeniedException("경기 결과를 추가할 권한이 없습니다."));
//
//        // 승인된 MatchRequest를 찾아서 awayClub으로 설정
//        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
//                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));
//
//        MatchResult matchResult = MatchResult.builder()
//                .matchPost(matchPost)
//                .homeClub(matchPost.getHomeClub())
//                .awayClub(acceptedRequest.getClub()) // 승인된 클럽을 away 팀으로 설정
//                .homeScore(homeScore)
//                .awayScore(awayScore)
//                .build();
//
//        return matchResultRepository.save(matchResult);
//    }
//
//
//    @Transactional
//    public void finalizeMatch(Long matchPostId, Long memberId) {
//
//        MatchPost matchPost = matchPostRepository.findById(matchPostId)
//                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
//
//        // 해당 클럽의 임원인지 확인
//        clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
//                        matchPost.getHomeClub().getClubId(), memberId)
//                .orElseThrow(() -> new AccessDeniedException("경기 결과를 추가할 권한이 없습니다."));
//
//        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
//
//        int homeWins = 0;
//        int awayWins = 0;
//
//        for (MatchResult result : matchResults) {
////            result.finalizeResult();
//            if (result.getHomeScore() > result.getAwayScore()) {
//                homeWins++;
//            } else if (result.getAwayScore() > result.getHomeScore()) {
//                awayWins++;
//            }
//        }
//
//        int homeScore = homeWins > awayWins ? 7 : homeWins == awayWins ? 5 : 3;
//        int awayScore = awayWins > homeWins ? 7 : homeWins == awayWins ? 5 : 3;
//
//        matchPost.getHomeClub().updateMatchScore(homeScore);
//
//        MatchRequest acceptedRequest = matchPost.getMatchRequests().stream()
//                .filter(request -> request.getStatus() == MatchingStatus.ACCEPTED)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("승인된 매칭 요청이 없습니다."));
//
//        acceptedRequest.getClub().updateMatchScore(awayScore);
//
//        matchPostRepository.save(matchPost);
//    }
//
//
//
//    @Transactional
//    public void confirmFinalResult(Long matchPostId, Long memberId) {
//        MatchPost matchPost = matchPostRepository.findById(matchPostId)
//                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
//
//        ClubMember clubMember = clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
//                        matchPost.getHomeClub().getClubId(), memberId)
//                .orElseThrow(() -> new AccessDeniedException("결과를 확정할 권한이 없습니다."));
//
//        matchPost.getMatchRequests().get(0).confirmByClub(clubMember.getClub());
//    }
//
//
//
//    @Transactional
//    public void rejectFinalResult(Long matchPostId, Long memberId) {
//        MatchPost matchPost = matchPostRepository.findById(matchPostId)
//                .orElseThrow(() -> new EntityNotFoundException("매칭 게시글을 찾을 수 없습니다."));
//
//        clubAdminMemberRepository.findActiveAdminByClubIdAndMemberId(
//                        matchPost.getHomeClub().getClubId(), memberId)
//                .orElseThrow(() -> new AccessDeniedException("결과를 거절할 권한이 없습니다."));
//
//        // 결과 거절 시, 수정 가능 상태로 만들기
//        List<MatchResult> matchResults = matchResultRepository.findByMatchPost(matchPost);
//        for (MatchResult result : matchResults) {
//            result.updateScores(null, null); // 점수를 초기화
//        }
//    }
//}
//
