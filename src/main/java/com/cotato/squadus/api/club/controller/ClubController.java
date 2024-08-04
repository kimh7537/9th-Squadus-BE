package com.cotato.squadus.api.club.controller;

import com.cotato.squadus.api.club.dto.ClubCreateRequest;
import com.cotato.squadus.api.club.dto.ClubCreateResponse;
import com.cotato.squadus.api.club.dto.ClubApplyRequest;
import com.cotato.squadus.api.club.dto.ClubApplyResponse;
import com.cotato.squadus.domain.club.common.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/create")
    public ResponseEntity<ClubCreateResponse> createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        ClubCreateResponse clubCreateResponse = clubService.createClub(clubCreateRequest);
        return ResponseEntity.ok(clubCreateResponse);
    }

    @PostMapping("/apply")
    public ResponseEntity<ClubApplyResponse> joinClub(@RequestBody ClubApplyRequest clubApplyRequest) {
        ClubApplyResponse clubApplyResponse = clubService.joinClub(clubApplyRequest);
        return ResponseEntity.ok(clubApplyResponse);
    }
}
