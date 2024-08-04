package com.cotato.squadus.api.club.controller;

import com.cotato.squadus.api.club.dto.ClubCreateRequest;
import com.cotato.squadus.api.club.dto.ClubCreateResponse;
import com.cotato.squadus.api.club.dto.ClubApplyRequest;
import com.cotato.squadus.api.club.dto.ClubApplyResponse;
import com.cotato.squadus.domain.club.common.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping()
    public ResponseEntity<ClubCreateResponse> createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        ClubCreateResponse clubCreateResponse = clubService.createClub(clubCreateRequest);
        return ResponseEntity.ok(clubCreateResponse);
    }

    @PostMapping("/{clubId}")
    public ResponseEntity<ClubApplyResponse> joinClub(@PathVariable Long clubId, @RequestBody ClubApplyRequest clubApplyRequest) {
        ClubApplyResponse clubApplyResponse = clubService.joinClub(clubId, clubApplyRequest);
        return ResponseEntity.ok(clubApplyResponse);
    }
}
