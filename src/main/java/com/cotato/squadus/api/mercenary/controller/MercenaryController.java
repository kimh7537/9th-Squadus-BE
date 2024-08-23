package com.cotato.squadus.api.mercenary.controller;

import com.cotato.squadus.api.match.dto.matchPost.request.FilterRequest;
import com.cotato.squadus.api.match.dto.matchPost.request.SearchRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryCreateRequest;
import com.cotato.squadus.api.mercenary.dto.request.MercenaryRequestRequest;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryCreateResponse;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryCreateResponseWrapper;
import com.cotato.squadus.api.mercenary.dto.response.MercenaryRequestResponse;
import com.cotato.squadus.domain.club.match.service.mercenary.MercenaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "용병 매치", description = "용병 매칭 관련 API")
@RestController
@RequestMapping("/v1/api/mercenary")
@RequiredArgsConstructor
public class MercenaryController {

    private final MercenaryService mercenaryService;

    @PostMapping
    @Operation(summary = "용병 매칭 생성", description = "용병 매칭 게시글을 생성합니다.")
    public ResponseEntity<MercenaryCreateResponse> createMatch(@RequestBody MercenaryCreateRequest mercenaryCreateRequest) {
        MercenaryCreateResponse response = mercenaryService.createMatch(mercenaryCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "모든 용병 매칭 조회", description = "모든 용병 매칭 게시글을 조회합니다")
    public ResponseEntity<MercenaryCreateResponseWrapper> getAllMatches() {
        List<MercenaryCreateResponse> responses = mercenaryService.findAllMatches();
        return ResponseEntity.ok(MercenaryCreateResponseWrapper.from(responses));
    }

    @GetMapping("/paged")
    @Operation(summary = "모든 용병 매칭 조회 (페이징)", description = "모든 용병 매칭 게시글을 페이징(10개) 처리하여 조회합니다.")
    public ResponseEntity<MercenaryCreateResponseWrapper> getAllMatchesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MercenaryCreateResponse> responses = mercenaryService.findAllMatches(pageable);
        return ResponseEntity.ok(MercenaryCreateResponseWrapper.from(responses));
    }

    //필터 4개를 한 번에 조회 가능. 필터를 하지 않는 부분은 null로 reqeust해주면 자동으로 필터링 해줌
    @PostMapping("/filter")
    @Operation(summary = "필터링된 용병 매칭 조회", description = "필터를 적용하여 용병 매칭 게시글을 조회합니다. 필터 4개를 한번에 조회할 수 있습니다. 필터를 하지 않는 부분은 null로 reqeust해주면 자동으로 필터링을 진행한다.")
    public ResponseEntity<MercenaryCreateResponseWrapper> getMatchesByFilter(
            @RequestBody FilterRequest filterRequest) {
        List<MercenaryCreateResponse> responses = mercenaryService.getFilteredMatches(filterRequest);
        return ResponseEntity.ok(MercenaryCreateResponseWrapper.from(responses));
    }

    @PostMapping("/search")
    @Operation(summary = "용병 매칭 검색", description = "검색어를 바탕으로 용병 매칭 게시글을 조회합니다.")
    public ResponseEntity<MercenaryCreateResponseWrapper> searchMatches(
            @RequestBody SearchRequest searchRequest) {
        List<MercenaryCreateResponse> responses = mercenaryService.searchMatches(searchRequest);
        return ResponseEntity.ok(MercenaryCreateResponseWrapper.from(responses));
    }

    @PostMapping("/request")
    @Operation(summary = "용병 매칭 요청", description = "특정 용병 매칭 게시글에 대해 매칭 요청을 보냅니다.")
    public ResponseEntity<MercenaryRequestResponse> sendMatchRequest(@RequestBody MercenaryRequestRequest mercenaryRequestRequest) {
        MercenaryRequestResponse response = mercenaryService.sendMatchRequest(mercenaryRequestRequest);
        return ResponseEntity.ok(response);
    }
}
