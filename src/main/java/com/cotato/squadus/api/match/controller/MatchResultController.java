package com.cotato.squadus.api.match.controller;

import com.cotato.squadus.api.match.dto.matchResult.response.MatchDetailResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchResultResponse;
import com.cotato.squadus.domain.club.match.service.match.MatchResultService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match-results")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchResultService matchResultService;

    @GetMapping("/{matchPostId}/details")
    @Operation(summary = "매칭 상세보기", description = "매칭이 승낙된 두 동아리의 상세 정보를 조회합니다.")
    public ResponseEntity<MatchDetailResponse> getMatchDetail(@PathVariable Long matchPostId) {
        MatchDetailResponse response = matchResultService.getMatchDetail(matchPostId);
        return ResponseEntity.ok(response);
    }

    //matchresult도 끌어와서 볼 수 있는 api만들기



    @PostMapping("/{matchPostId}/add-result")
    @Operation(summary = "매치 결과 추가", description = "경기 결과를 추가합니다.")
    public ResponseEntity<MatchResultResponse> addMatchResult(
            @PathVariable Long matchPostId,
            @RequestParam Long memberId,
            @RequestParam Integer homeScore,
            @RequestParam Integer awayScore) {
        MatchResultResponse matchResult = matchResultService.addMatchResult(matchPostId, memberId, homeScore, awayScore);
        return ResponseEntity.ok(matchResult);
    }

    //모든 경기 입력 완료 버튼을 누르면 합산 결과를 포함한 결과 값이 나옴
    @PostMapping("/{matchPostId}/finalize")
    @Operation(summary = "경기 결과 확정", description = "경기 결과를 확정하고 점수를 반영합니다.")
    public ResponseEntity<Void> finalizeMatch(
            @PathVariable Long matchPostId,
            @RequestParam Long memberId) {
        matchResultService.finalizeMatch(matchPostId, memberId);
        return ResponseEntity.noContent().build();
    }
    
    //Home팀의 Admin이 결과를 확정하는 api
    //Home 팀의 Admin이 결과를 확정하면 모든 mathResult에서 isFinalizedHome값이 true로 변경됨
    @PostMapping("/{matchPostId}/finalize-away")
    @Operation(summary = "어웨이팀 경기 결과 확정", description = "어웨이팀이 경기 결과를 확정하고 점수를 반영합니다.")
    public ResponseEntity<Void> finalizeAwayMatch(
            @PathVariable Long matchPostId,
            @RequestParam Long memberId) {
        matchResultService.finalizeAwayResult(matchPostId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matchPostId}/reject")
    @Operation(summary = "경기 결과 거절", description = "경기 결과를 거절하고 수정 가능하게 만듭니다.")
    public ResponseEntity<Void> rejectFinalResult(
            @PathVariable Long matchPostId,
            @RequestParam Long memberId) {
        matchResultService.rejectFinalResult(matchPostId, memberId);
        return ResponseEntity.noContent().build();
    }

}

