package com.cotato.squadus.api.mercenary.controller;

import com.cotato.squadus.api.mercenary.dto.request.MercenaryCreateRequest;
import com.cotato.squadus.api.mercenary.dto.response.*;
import com.cotato.squadus.domain.club.match.service.MercenaryRequestService;
import com.cotato.squadus.domain.club.match.service.MercenaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/mercenary-requests")
@RequiredArgsConstructor
@Tag(name = "용병 매칭 요청", description = "용병 매칭 요청 관련 API")
public class MercenaryRequestController {

    private final MercenaryRequestService mercenaryRequestService;
    private final MercenaryService mercenaryService;

    //신청한 내역 기능 - 개인 단위의 신청
    @GetMapping("/paged")
    @Operation(summary = "개인이 신청한 용병 매치 목록 조회 (페이징)", description = "개인이 신청한 용병 매칭 글 목록을 페이징 처리하여 조회합니다.")
    public ResponseEntity<MercenaryRequestResponseWrapper> getMyRequests(
            @RequestParam Long clubMemberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MercenaryRequestResponse> responses = mercenaryRequestService.getMyRequests(clubMemberId, pageable);
        return ResponseEntity.ok(MercenaryRequestResponseWrapper.from(responses));
    }

    @GetMapping
    @Operation(summary = "개인이 신청한 용병 매치 목록 조회 (전체)", description = "개인이 신청한 용병 매칭 글 목록을 페이징 없이 전체 조회합니다.")
    public ResponseEntity<MercenaryRequestResponseWrapper> getAllMyRequests(@RequestParam Long clubMemberId) {
        List<MercenaryRequestResponse> responses = mercenaryRequestService.getAllMyRequests(clubMemberId);
        return ResponseEntity.ok(MercenaryRequestResponseWrapper.from(responses));
    }



    @DeleteMapping("/{requestId}")
    @Operation(summary = "용병 매칭 요청 취소", description = "개인이 요청한 용병 매칭 요청을 취소합니다.")
    public ResponseEntity<Void> cancelMercenaryRequest(@PathVariable Long requestId, @RequestParam Long clubMemberId) {
        mercenaryRequestService.cancelMatchRequest(requestId, clubMemberId);
        return ResponseEntity.noContent().build();
    }


    //신청 받은 내역 기능
    @GetMapping("/received/paged")
    @Operation(summary = "내 동아리가 받은 용병 매칭 요청 목록 조회 (페이징)", description = "내 동아리가 받은 용병 매칭 요청 목록을 페이징 처리하여 조회합니다.")
    public ResponseEntity<MercenaryRequestAndMercenaryPostResponseWrapper> getReceivedMatchRequests(
            @RequestParam Long clubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MercenaryRequestAndMercenaryPostResponse> responses = mercenaryRequestService.getReceivedMatchRequests(clubId, pageable);
        MercenaryRequestAndMercenaryPostResponseWrapper wrapper = MercenaryRequestAndMercenaryPostResponseWrapper.from(responses.getContent());
        return ResponseEntity.ok(wrapper);
    }

    @GetMapping("/received")
    @Operation(summary = "내 동아리가 받은 용병 매칭 요청 목록 조회 (전체)", description = "내 동아리가 받은 용병 매칭 요청 목록을 페이징 없이 전체 조회합니다.")
    public ResponseEntity<MercenaryRequestAndMercenaryPostResponseWrapper> getAllReceivedMatchRequests(@RequestParam Long clubId) {
        List<MercenaryRequestAndMercenaryPostResponse> responses = mercenaryRequestService.getAllReceivedMatchRequests(clubId);
        MercenaryRequestAndMercenaryPostResponseWrapper wrapper = MercenaryRequestAndMercenaryPostResponseWrapper.from(responses);
        return ResponseEntity.ok(wrapper);
    }


    @PostMapping("/{requestId}/decision")
    @Operation(summary = "매칭 요청 승낙/거절", description = "특정 동아리의 임원이 받은 매칭 요청에 대해 승낙 또는 거절을 합니다.")
    public ResponseEntity<Void> decideMatchRequest(@PathVariable Long requestId, @RequestParam String decision, @RequestParam Long clubMemberId) {
        mercenaryRequestService.decideMatchRequest(requestId, decision, clubMemberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{mercenaryIdx}")
    @Operation(summary = "용병 매칭 게시글 수정", description = "특정 용병 매칭 게시글을 수정합니다.")
    public ResponseEntity<MercenaryCreateResponse> updateMercenaryPost(
            @PathVariable Long mercenaryIdx,
            @RequestBody MercenaryCreateRequest mercenaryCreateRequest) {
        MercenaryCreateResponse response = mercenaryService.updateMercenaryPost(mercenaryIdx, mercenaryCreateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{mercenaryIdx}")
    @Operation(summary = "용병 매칭 게시글 삭제", description = "특정 용병 매칭 게시글을 삭제합니다.")
    public ResponseEntity<Void> deleteMercenaryPost(
            @PathVariable Long mercenaryIdx,
            @RequestParam Long clubMemberId) {
        mercenaryService.deleteMercenaryPost(mercenaryIdx, clubMemberId);
        return ResponseEntity.noContent().build();
    }
}
