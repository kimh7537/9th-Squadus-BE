package com.cotato.squadus.api.fee.controller;

import com.cotato.squadus.api.fee.dto.*;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.club.fee.service.ClubFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "동아리 회비", description = "동아리 회비 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/fees")
@RequiredArgsConstructor
public class ClubFeeController {

    private final ClubFeeService clubFeeService;


    @GetMapping("")
    @Operation(summary = "동아리 회비 종류 전체 요약 조회", description = "동아리 회비의 종류 전체를 요약해서 조회합니다.(정기회비, 이벤트회비 등)")
    public ResponseEntity<ClubFeeSummaryResponseList> findAllClubFeesSummary(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("clubId") Long clubId) {
        ClubFeeSummaryResponseList clubFeeSummaryResponseList = clubFeeService.findAllClubFeeTypesSummary(customOAuth2Member, clubId);
        return ResponseEntity.ok(clubFeeSummaryResponseList);
    }

    @PostMapping("")
    @Operation(summary = "동아리 회비 등록", description = "동아리 회비를 등록합니다.")
    public ResponseEntity<ClubFeeCreateResponse> createClubFee(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("clubId") Long clubId, ClubFeeCreateRequest clubFeeCreateRequest) {
        ClubFeeCreateResponse createdClubFee = clubFeeService.createFee(customOAuth2Member, clubId, clubFeeCreateRequest);
        return ResponseEntity.ok(createdClubFee);
    }

    @PostMapping("/{feeTypeId}/usage")
    @Operation(summary = "동아리 회비 사용", description = "회비의 id를 지정하여 동아리 회비를 사용합니다.")
    public ResponseEntity<ClubFeeUsageResponse> createClubFeeUsage(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("clubId") Long clubId, @PathVariable("feeTypeId") Long feeTypeId, ClubFeeUsageRequest clubFeeUsageRequest) {
        ClubFeeUsageResponse clubFeeUsage = clubFeeService.createClubFeeUsage(customOAuth2Member, clubId, feeTypeId, clubFeeUsageRequest);
        return ResponseEntity.ok(clubFeeUsage);
    }

    @GetMapping("/{feeTypeId}/payment")
    @Operation(summary = "동아리 회비 입금 현황 조회", description = "회비의 id를 지정하여 동아리의 입금 현황을 조회합니다.")
    public ResponseEntity<ClubFeePaymentInfoResponseList> findClubFeePaymentInfo(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member,
            @PathVariable("clubId") Long clubId,
            @PathVariable("feeTypeId") Long feeTypeId) {
        ClubFeePaymentInfoResponseList clubFeePaymentInfo = clubFeeService.findClubFeePaymentInfo(customOAuth2Member, clubId, feeTypeId);
        return ResponseEntity.ok(clubFeePaymentInfo);
    }







}
