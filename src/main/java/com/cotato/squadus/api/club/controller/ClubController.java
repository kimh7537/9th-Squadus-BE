package com.cotato.squadus.api.club.controller;

import com.cotato.squadus.api.club.dto.*;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.club.common.service.ClubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "동아리", description = "동아리 관련 API")
@RestController
@RequestMapping("/v1/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final ClubMemberService clubMemberService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "동아리 생성", description = "동아리에 대한 정보를 바탕으로 동아리를 생성합니다")
    public ResponseEntity<ClubCreateResponse> createClub(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member,
            @Parameter(description = "동아리 생성 요청 정보", schema = @Schema(implementation = ClubCreateRequest.class))
            @RequestPart("clubCreateRequest") String clubCreateRequestString,
            @RequestPart(value = "logoImage", required = false) MultipartFile logoImage) {

        // JSON String을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ClubCreateRequest clubCreateRequest;
        try {
            clubCreateRequest = objectMapper.readValue(clubCreateRequestString, ClubCreateRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
        ClubCreateResponse clubCreateResponse = clubService.createClub(customOAuth2Member, clubCreateRequest, logoImage);
        return ResponseEntity.ok(clubCreateResponse);
    }

    @GetMapping("/{clubId}")
    @Operation(summary = "동아리 기본 정보 조회", description = "clubId를 바탕으로 동아리에 대한 정보를 반환합니다.")
    public ResponseEntity<ClubInfoResponse> findClubInfo(@PathVariable Long clubId) {
        ClubInfoResponse clubInfoResponse = clubService.findClubInfo(clubId);
        return ResponseEntity.ok(clubInfoResponse);
    }

    @GetMapping("/{clubId}/members")
    @Operation(summary = "동아리원 전체 조회", description = "clubId를 바탕으로 동아리원을 조회합니다.")
    public ResponseEntity<ClubMemberInfoResponseList> findAllClubMemberInfo(@PathVariable Long clubId) {
        ClubMemberInfoResponseList clubMemberInfoResponseList = clubMemberService.findAllClubMemberInfo(clubId);
        return ResponseEntity.ok(clubMemberInfoResponseList);
    }


    @PostMapping("/{clubId}")
    @Operation(summary = "동아리 가입 신청", description = "clubId와 동아리 가입에 대한 정보를 바탕으로 동아리 가입을 신청합니다")
    public ResponseEntity<ClubApplyResponse> joinClub(@PathVariable Long clubId, @RequestBody ClubApplyRequest clubApplyRequest) {
        ClubApplyResponse clubApplyResponse = clubService.joinClub(clubId, clubApplyRequest);
        return ResponseEntity.ok(clubApplyResponse);
    }

//    @PatchMapping("/{clubId}")
//    @Operation(summary = "동아리 기본정보 수정", description = "동아리의 기본 정보를 변경합니다.")
//    public ResponseEntity<ClubUpdateResponse> updateClub(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long clubId, @RequestBody ClubUpdateRequest clubUpdateRequest) {
//        ClubUpdateResponse clubUpdateResponse = clubService.updateClub(customOAuth2Member, clubId, clubUpdateRequest);
//        return ResponseEntity.ok(clubUpdateResponse);
//    }

    @PatchMapping(value = "/{clubId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "동아리 기본정보 수정", description = "동아리의 기본 정보를 변경합니다.")
    public ResponseEntity<ClubUpdateResponse> updateClub(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member,
            @PathVariable Long clubId,
            @Parameter(description = "동아리 수정 요청 정보", schema = @Schema(implementation = ClubUpdateRequest.class))
            @RequestPart("clubUpdateRequest") String clubUpdateRequestString,
            @RequestPart(value = "logoImage", required = false) MultipartFile logoImage) {

        // JSON String을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ClubUpdateRequest clubUpdateRequest;
        try {
            clubUpdateRequest = objectMapper.readValue(clubUpdateRequestString, ClubUpdateRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
        ClubUpdateResponse clubUpdateResponse = clubService.updateClub(customOAuth2Member, clubId, clubUpdateRequest, logoImage);
        return ResponseEntity.ok(clubUpdateResponse);
    }
}
