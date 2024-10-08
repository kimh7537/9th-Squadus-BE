package com.cotato.squadus.api.match.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cotato.squadus.api.match.dto.matchPost.request.MatchCreateRequest;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestAndMatchPostResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestAndMatchPostResponseWrapper;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponseWrapper;
import com.cotato.squadus.domain.club.match.service.match.MatchRequestService;
import com.cotato.squadus.domain.club.match.service.match.MatchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/api/match-requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "매칭 요청", description = "매칭 요청 관련 API")
public class MatchRequestController {

	private final MatchRequestService matchRequestService;
	private final MatchService matchService;

	//신청한 내역 기능 - 동아리 단위의 신청
	@GetMapping("/paged")
	@Operation(summary = "내 동아리에서 신청한 매치 목록 조회 (페이징)", description = "내 동아리에서 신청한 매칭 글 목록을 페이징 처리하여 조회합니다.")
	public ResponseEntity<MatchRequestResponseWrapper> getMyClubMatchRequests(
		@RequestParam Long clubId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<MatchRequestResponse> responses = matchRequestService.getMyClubMatchRequests(clubId, pageable);
		return ResponseEntity.ok(MatchRequestResponseWrapper.from(responses));
	}

	@GetMapping
	@Operation(summary = "내 동아리에서 신청한 매치 목록 조회 (전체)", description = "내 동아리에서 신청한 매칭 글 목록을 페이징 없이 전체 조회합니다.")
	public ResponseEntity<MatchRequestResponseWrapper> getAllMyClubMatchRequests(@RequestParam Long clubId) {
		List<MatchRequestResponse> responses = matchRequestService.getAllMyClubMatchRequests(clubId);
		return ResponseEntity.ok(MatchRequestResponseWrapper.from(responses));
	}

	@DeleteMapping("/{requestId}")
	@Operation(summary = "매칭 요청 취소", description = "특정 동아리의 임원이 요청한 매칭 요청을 취소합니다.")
	public ResponseEntity<Void> cancelMatchRequest(@PathVariable Long requestId, @RequestParam Long clubMemberId) {
		matchRequestService.cancelMatchRequest(requestId, clubMemberId);
		return ResponseEntity.noContent().build();
	}

	//신청 받은 내역 기능
	@GetMapping("/received/paged")
	@Operation(summary = "내 동아리가 받은 매칭 요청 목록 조회 (페이징)", description = "내 동아리가 받은 매칭 요청 목록을 페이징 처리하여 조회합니다.")
	public ResponseEntity<MatchRequestAndMatchPostResponseWrapper> getReceivedMatchRequests(
		@RequestParam Long clubId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<MatchRequestAndMatchPostResponse> responses = matchRequestService.getReceivedMatchRequests(clubId,
			pageable);
		MatchRequestAndMatchPostResponseWrapper wrapper = MatchRequestAndMatchPostResponseWrapper.from(
			responses.getContent());
		return ResponseEntity.ok(wrapper);
	}

	@GetMapping("/received")
	@Operation(summary = "내 동아리가 받은 매칭 요청 목록 조회 (전체)", description = "내 동아리가 받은 매칭 요청 목록을 페이징 없이 전체 조회합니다.")
	public ResponseEntity<MatchRequestAndMatchPostResponseWrapper> getAllReceivedMatchRequests(
		@RequestParam Long clubId) {
		List<MatchRequestAndMatchPostResponse> responses = matchRequestService.getAllReceivedMatchRequests(clubId);
		MatchRequestAndMatchPostResponseWrapper wrapper = MatchRequestAndMatchPostResponseWrapper.from(responses);
		return ResponseEntity.ok(wrapper);
	}

	@PostMapping("/{requestId}/decision")
	@Operation(summary = "매칭 요청 승낙/거절", description = "특정 동아리의 임원이 받은 매칭 요청에 대해 승낙 또는 거절을 합니다.")
	public ResponseEntity<Void> decideMatchRequest(@PathVariable Long requestId, @RequestParam String decision,
		@RequestParam Long clubMemberId) {
		matchRequestService.decideMatchRequest(requestId, decision, clubMemberId);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{matchIdx}")
	@Operation(summary = "매칭 게시글 수정", description = "특정 매칭 게시글을 수정합니다.")
	public ResponseEntity<MatchCreateResponse> updateMatchPost(
		@PathVariable Long matchIdx,
		@RequestBody MatchCreateRequest matchCreateRequest) {
		MatchCreateResponse response = matchService.updateMatchPost(matchIdx, matchCreateRequest);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{matchIdx}/delete")
	@Operation(summary = "매칭 게시글 삭제", description = "특정 매칭 게시글을 삭제합니다.")
	public ResponseEntity<Void> deleteMatchPost(
		@PathVariable Long matchIdx,
		@RequestParam Long clubMemberId) {
		matchService.deleteMatchPost(matchIdx, clubMemberId);
		return ResponseEntity.noContent().build();
	}
}

