package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.domain.club.post.service.ClubPostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/posts/{postId}/comments")
@RequiredArgsConstructor
public class ClubPostCommentController {

    private final ClubPostCommentService clubPostCommentService;

    @GetMapping()
    public void getAllClubPostComments(@PathVariable Long clubId, @PathVariable Long postId) {

    }
}
