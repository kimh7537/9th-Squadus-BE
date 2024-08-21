package com.cotato.squadus.api.match.controller;

import com.cotato.squadus.api.match.dto.matchResult.request.MatchResultAddRequest;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchDetailResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchDetailWithWinResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchFinalResultResponse;
import com.cotato.squadus.api.match.dto.matchResult.response.MatchResultResponse;
import com.cotato.squadus.domain.club.match.service.match.MatchResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "매치 결과", description = "매칭 결과 관련 API")
@RestController
@RequestMapping("/v1/api/match-results")
@RequiredArgsConstructor
public class MatchResultController {

    private final MatchResultService matchResultService;

    @GetMapping("/{matchPostId}/details")
    @Operation(summary = "매칭 상세보기", description = "매칭이 승낙된 두 동아리의 상세 정보를 조회합니다.")
    public ResponseEntity<MatchDetailResponse> getMatchDetail(@PathVariable Long matchPostId) {
        MatchDetailResponse response = matchResultService.getMatchDetail(matchPostId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{matchPostId}/details-with-win")
    @Operation(summary = "매칭 상세보기 및 승리 정보", description = "매칭이 승낙된 두 동아리의 상세 정보와 승리 횟수를 조회합니다.")
    public ResponseEntity<MatchDetailWithWinResponse> getMatchDetailWithWin(@PathVariable Long matchPostId) {
        MatchDetailResponse matchDetailResponse = matchResultService.getMatchDetail(matchPostId);
        MatchFinalResultResponse matchFinalResultResponse = matchResultService.getWinningMatchResult(matchPostId);

        MatchDetailWithWinResponse response = new MatchDetailWithWinResponse(matchDetailResponse, matchFinalResultResponse);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{matchPostId}/add-result")
    @Operation(summary = "매치 결과 추가", description = "경기 결과를 추가합니다. MatchPost 작성 Club의 임원만 추가 가능합니다.")
    public ResponseEntity<MatchResultResponse> addMatchResult(@PathVariable Long matchPostId, @RequestBody MatchResultAddRequest matchResultAddRequest) {
        MatchResultResponse matchResult = matchResultService.addMatchResult(matchPostId, matchResultAddRequest);
        return ResponseEntity.ok(matchResult);
    }

    @PutMapping("/{matchResultId}/update-result")
    @Operation(summary = "매치 결과 수정", description = "경기 결과를 수정합니다. MatchPost 작성 Club의 임원만 수정 가능합니다.")
    public ResponseEntity<MatchResultResponse> updateMatchResult(
            @PathVariable Long matchResultId,
            @RequestBody MatchResultAddRequest matchResultAddRequest) {
        MatchResultResponse matchResult = matchResultService.updateMatchResult(matchResultId, matchResultAddRequest);
        return ResponseEntity.ok(matchResult);
    }

    
    @GetMapping("/{matchPostId}/final-result")
    @Operation(summary = "매치 승리 결과", description = "매치의 최종 승리 결과를 가져옵니다. MatchPost 작성 Club의 임원만 추가 가능합니다. 모든 경기 입력 완료 버튼")
    public ResponseEntity<MatchFinalResultResponse> getFinalMatchResult(@PathVariable Long matchPostId, @RequestParam Long clubMemberId)  {
        MatchFinalResultResponse response = matchResultService.getFinalMatchResult(matchPostId, clubMemberId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{matchPostId}/finalize-home")
    @Operation(summary = "홈팀 경기 결과 확정", description = "홈팀이 경기 결과를 확정하고 점수를 반영합니다. MatchPost 작성 Club의 임원만 추가 가능합니다. ")
    public ResponseEntity<Void> finalizeHomeMatch(
            @PathVariable Long matchPostId,
            @RequestParam Long clubMemberId) {
        matchResultService.finalizeHomeResult(matchPostId, clubMemberId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{matchPostId}/finalize-away")
    @Operation(summary = "어웨이팀 경기 결과 확정", description = "어웨이팀이 경기 결과를 확정하고 점수를 반영합니다. Away Club의 임원만 추가 가능합니다.")
    public ResponseEntity<Void> finalizeAwayMatch(
            @PathVariable Long matchPostId,
            @RequestParam Long clubMemberId) {
        matchResultService.finalizeAwayResult(matchPostId, clubMemberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{matchPostId}/reject")
    @Operation(summary = "경기 결과 거절", description = "경기 결과를 거절하고 수정 가능하게 만듭니다. Away Club의 임원만 추가 가능합니다.")
    public ResponseEntity<Void> rejectFinalResult(
            @PathVariable Long matchPostId,
            @RequestParam Long clubMemberId) {
        matchResultService.rejectFinalResult(matchPostId, clubMemberId);
        return ResponseEntity.noContent().build();
    }

}

