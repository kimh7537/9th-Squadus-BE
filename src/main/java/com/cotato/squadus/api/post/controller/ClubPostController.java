package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.api.post.dto.ClubPostListResponse;
import com.cotato.squadus.api.post.dto.ClubPostResponse;
import com.cotato.squadus.api.post.dto.ClubPostSummaryListResponse;
import com.cotato.squadus.domain.club.post.service.ClubPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/posts")
@RequiredArgsConstructor
public class ClubPostController {

    private final ClubPostService clubPostService;

    @GetMapping("")
    public ResponseEntity<ClubPostListResponse> getAllClubPostsByClubId(@RequestParam("club-id") Long clubId) {
        ClubPostListResponse allClubPostsByClubId = clubPostService.findAllClubPostsByClubId(clubId);
        log.info("ClubId로 동아리 공지 전체 조회 : {} ", allClubPostsByClubId);
        return ResponseEntity.ok(allClubPostsByClubId);
    }

    @GetMapping("/summary")
    public ResponseEntity<ClubPostSummaryListResponse> getAllClubPostsSummaryByClubId(@RequestParam("club-id") Long clubId) {
        ClubPostSummaryListResponse allClubPostsSummaryByClubId = clubPostService.findAllClubPostsSummaryByClubId(clubId);
        log.info("ClubId로 동아리 공지 전체 조회(제목, 날짜) : {} ", allClubPostsSummaryByClubId);
        return ResponseEntity.ok(allClubPostsSummaryByClubId);
    }

    @GetMapping("{postId}")
    public ResponseEntity<ClubPostResponse> getPostByPostId(@PathVariable Long postId) {
        ClubPostResponse clubPostByPostId = clubPostService.findClubPostByPostId(postId);
        log.info("PostId로 동아리 공지 조회 : {} ", clubPostByPostId);
        return ResponseEntity.ok(clubPostByPostId);
    }

}
