package com.cotato.squadus.api.match.controller;

import com.cotato.squadus.api.match.dto.match.request.*;
import com.cotato.squadus.api.match.dto.match.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.match.response.MatchCreateResponseWrapper;
import com.cotato.squadus.api.match.dto.match.response.MatchRequestResponse;
import com.cotato.squadus.domain.club.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "매치", description = "매칭 관련 API")
@RestController
@RequestMapping("/v1/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @Operation(summary = "매칭 생성", description = "매칭 게시글을 생성합니다.")
    public ResponseEntity<MatchCreateResponse> createMatch(@RequestBody MatchCreateRequest matchCreateRequest) {
        MatchCreateResponse response = matchService.createMatch(matchCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "모든 매칭 조회", description = "모든 매칭 게시글을 조회합니다.")
    public ResponseEntity<MatchCreateResponseWrapper> getAllMatches() {
        List<MatchCreateResponse> responses = matchService.findAllMatches();
        return ResponseEntity.ok(MatchCreateResponseWrapper.from(responses));
    }

    @GetMapping("/paged")
    @Operation(summary = "모든 매칭 조회 (페이징)", description = "모든 매칭 게시글을 페이징 처리하여 조회합니다.")
    public ResponseEntity<MatchCreateResponseWrapper> getAllMatchesPaged(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MatchCreateResponse> responses = matchService.findAllMatches(pageable);
        return ResponseEntity.ok(MatchCreateResponseWrapper.from(responses));
    }

    //필터 4개를 한 번에 조회 가능. 필터를 하지 않는 부분은 null로 reqeust해주면 자동으로 필터링 해줌
    @PostMapping("/filter")
    @Operation(summary = "필터링된 매칭 조회", description = "필터를 적용하여 매칭 게시글을 조회합니다. 필터 4개를 한번에 조회할 수 있습니다.")
    public ResponseEntity<MatchCreateResponseWrapper> getMatchesByFilter(@RequestBody MatchFilterRequest filterRequest) {
        List<MatchCreateResponse> responses = matchService.getFilteredMatches(filterRequest);
        return ResponseEntity.ok(MatchCreateResponseWrapper.from(responses));
    }

    @PostMapping("/search")
    @Operation(summary = "매칭 검색", description = "검색어를 바탕으로 매칭 게시글을 조회합니다.")
    public ResponseEntity<MatchCreateResponseWrapper> searchMatches(@RequestBody MatchSearchRequest searchRequest) {
        List<MatchCreateResponse> responses = matchService.searchMatches(searchRequest);
        return ResponseEntity.ok(MatchCreateResponseWrapper.from(responses));
    }

    @PostMapping("/request")
    @Operation(summary = "매칭 요청", description = "특정 매칭 게시글에 대해 매칭 요청을 보냅니다.")
    public ResponseEntity<MatchRequestResponse> sendMatchRequest(@RequestBody MatchRequestRequest matchRequestRequest) {
        MatchRequestResponse response = matchService.sendMatchRequest(matchRequestRequest);
        return ResponseEntity.ok(response);
    }
}
