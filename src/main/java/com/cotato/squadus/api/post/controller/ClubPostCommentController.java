package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.api.post.dto.ClubPostCommentListResponse;
import com.cotato.squadus.api.post.dto.ClubPostCommentResponse;
import com.cotato.squadus.domain.club.post.service.ClubPostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/posts/{postId}/comments")
@RequiredArgsConstructor
public class ClubPostCommentController {

    private final ClubPostCommentService clubPostCommentService;

    @GetMapping()
    public ResponseEntity<ClubPostCommentListResponse> getAllClubPostComments(@PathVariable Long clubId, @PathVariable Long postId) {
        List<ClubPostCommentResponse> allClubPostComments = clubPostCommentService.findAllClubPostComments(postId);
        log.info("공지에 대한 전체 댓글 조회 : {}", allClubPostComments);
        return ResponseEntity.ok(ClubPostCommentListResponse.from(allClubPostComments));
    }
}
