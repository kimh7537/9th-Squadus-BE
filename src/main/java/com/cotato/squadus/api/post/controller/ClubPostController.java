package com.cotato.squadus.api.post.controller;

import com.cotato.squadus.api.post.dto.ClubPostListResponse;
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
        ClubPostListResponse allClubPostsByClubId = clubPostService.findAllClubPostsByClubId(clubId.longValue());
        log.info(allClubPostsByClubId.toString());
        return ResponseEntity.ok(allClubPostsByClubId);
    }

}
