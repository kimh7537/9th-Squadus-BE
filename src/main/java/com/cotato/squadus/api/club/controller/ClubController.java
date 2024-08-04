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

@Tag(name = "동아리", description = "동아리 관련 API")
@RestController
@RequestMapping("/v1/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("/create")
    @Operation(summary = "동아리 생성", description = "동아리에 대한 정보를 통해 동아리를 생성합니다")
    public ResponseEntity<ClubCreateResponse> createClub(@RequestBody ClubCreateRequest clubCreateRequest) {
        ClubCreateResponse clubCreateResponse = clubService.createClub(clubCreateRequest);
        return ResponseEntity.ok(clubCreateResponse);
    }

    @PostMapping("/apply")
    @Operation(summary = "동아리 가입 신청", description = "유저의 id, 동아리의 id를 통해 동아리에 가입을 신청합니다")
    public ResponseEntity<ClubApplyResponse> joinClub(@RequestBody ClubApplyRequest clubApplyRequest) {
        ClubApplyResponse clubApplyResponse = clubService.joinClub(clubApplyRequest);
        return ResponseEntity.ok(clubApplyResponse);
    }
}
