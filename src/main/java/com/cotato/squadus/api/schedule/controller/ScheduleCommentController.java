package com.cotato.squadus.api.schedule.controller;

import java.util.List;

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

import com.cotato.squadus.api.schedule.dto.LikeResponse;
import com.cotato.squadus.api.schedule.dto.ScheduleCommentListResponse;
import com.cotato.squadus.api.schedule.dto.ScheduleCommentRequest;
import com.cotato.squadus.api.schedule.dto.ScheduleCommentResponse;
import com.cotato.squadus.domain.club.schedule.service.ScheduleCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class ScheduleCommentController {

	private final ScheduleCommentService commentService;

	@PostMapping
	public ResponseEntity<ScheduleCommentResponse> addComment(@PathVariable Long scheduleId,
		@RequestBody ScheduleCommentRequest commentRequest) {
		ScheduleCommentResponse commentResponse = commentService.addComment(scheduleId, commentRequest);
		return ResponseEntity.ok(commentResponse);
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<ScheduleCommentResponse> updateComment(@PathVariable Long scheduleId,
		@PathVariable Long commentId, @RequestBody ScheduleCommentRequest commentUpdateRequest) {
		ScheduleCommentResponse commentResponse = commentService.updateComment(scheduleId, commentId,
			commentUpdateRequest);
		return ResponseEntity.ok(commentResponse);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId,
		@RequestParam Long memberId) {
		commentService.deleteComment(scheduleId, commentId, memberId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<ScheduleCommentListResponse> getAllComments(@PathVariable Long scheduleId) {
		List<ScheduleCommentResponse> comments = commentService.getAllComments(scheduleId);

		return ResponseEntity.ok(ScheduleCommentListResponse.from(comments));
	}

	@PostMapping("/{commentId}/like")
	public ResponseEntity<LikeResponse> likeComment(@PathVariable Long scheduleId, @PathVariable Long commentId) {
		LikeResponse likeResponse = commentService.likeComment(scheduleId, commentId);
		return ResponseEntity.ok(likeResponse);
	}
}
