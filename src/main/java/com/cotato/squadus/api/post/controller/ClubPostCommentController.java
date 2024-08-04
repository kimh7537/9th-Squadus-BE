package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.api.post.dto.*;
import com.cotato.squadus.domain.club.post.service.ClubPostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "동아리 공지 댓글", description = "동아리 공지 댓글 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/posts/{postId}/comments")
@RequiredArgsConstructor
public class ClubPostCommentController {

    private final ClubPostCommentService clubPostCommentService;

    @GetMapping()
    @Operation(summary = "동아리 공지 댓글 전체 조회", description = "clubId와 postId를 바탕으로 동아리 공지 댓글 전체를 조회합니다")
    public ResponseEntity<ClubPostCommentListResponse> getAllClubPostComments(@PathVariable Long clubId, @PathVariable Long postId) {
        List<ClubPostCommentResponse> allClubPostComments = clubPostCommentService.findAllClubPostComments(postId);
        log.info("공지에 대한 전체 댓글 조회 : {}", allClubPostComments);
        return ResponseEntity.ok(ClubPostCommentListResponse.from(allClubPostComments));
    }

    @PostMapping()
    @Operation(summary = "동아리 공지 댓글 생성", description = "clubId와 postId를 바탕으로 동아리 공지 댓글을 하나 생성합니다")
    public ResponseEntity<ClubPostCommentCreateResponse> createClubPostComment(
            @PathVariable Long clubId,
            @PathVariable Long postId,
            @RequestBody ClubPostCommentCreateRequest clubPostCommentCreateRequest) {

        ClubPostCommentCreateResponse clubPostCommentCreateResponse = clubPostCommentService.createClubPostComment(clubId, postId, clubPostCommentCreateRequest);
        return ResponseEntity.ok(clubPostCommentCreateResponse);
    }

    @PatchMapping("/{commentId}/like")
    @Operation(summary = "동아리 공지 댓글 좋아요", description = "clubId, postId, commentId를 바탕으로 동아리 공지 댓글에 대한 좋아요를 1 증가시킵니다")
    public ResponseEntity<ClubPostCommentLikeResponse> increaseClubPostCommentLike(@PathVariable Long clubId, @PathVariable Long postId, @PathVariable Long commentId) {

        ClubPostCommentLikeResponse clubPostCommentLikeResponse = clubPostCommentService.increaseClubPostCommentLike(commentId);
        return ResponseEntity.ok(clubPostCommentLikeResponse);
    }
}
