package com.cotato.squadus.controller;

import com.cotato.squadus.dto.ClubCreateRequest;
import com.cotato.squadus.dto.ClubCreateResponse;
import com.cotato.squadus.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/create")
    public ResponseEntity<ClubCreateResponse> createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        ClubCreateResponse clubCreateResponse = clubService.createClub(clubCreateRequest);
        return ResponseEntity.ok(clubCreateResponse);
    }
}
