package com.cotato.squadus.api.admin.controller;

import com.cotato.squadus.api.admin.dto.ClubApplicationListResponse;
import com.cotato.squadus.api.admin.dto.ClubJoinApprovalResponse;
import com.cotato.squadus.api.admin.dto.ClubJoinDenialResponse;
import com.cotato.squadus.domain.club.admin.service.ClubAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "동아리 관리", description = "동아리 관리 관련 API")
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/admin")
@RequiredArgsConstructor
public class ClubAdminController {

    private final ClubAdminService clubAdminService;

    @PostMapping("/approval/{applicationId}")
    @Operation(summary = "동아리 가입 신청 승인", description = "ADMIN 회원인지 검증 후 applicationId를 통해 가입을 승인합니다")
    public ResponseEntity<ClubJoinApprovalResponse> approveClubMember(@PathVariable Long clubId, @PathVariable Long applicationId) {
        ClubJoinApprovalResponse clubJoinApprovalResponse = clubAdminService.approveApply(clubId, applicationId);
        return ResponseEntity.ok(clubJoinApprovalResponse);
    }

    @PostMapping("/denial/{applicationId}")
    @Operation(summary = "동아리 가입 신청 거절", description = "applicationId를 통해 가입을 거절합니다.")
    public ResponseEntity<ClubJoinDenialResponse> denyClubMember(@PathVariable Long clubId, @PathVariable Long applicationId) {
        ClubJoinDenialResponse clubJoinDenialResponse = clubAdminService.denyApply(clubId, applicationId);
        return ResponseEntity.ok(clubJoinDenialResponse);
    }

    @GetMapping("/applications/{recruitingPostId}")
    @Operation(summary = "동아리 가입 신청 내역 조회", description = "clubId를 통해 해당 동아리의 가입 신청 내역을 조회합니다.")
    public ResponseEntity<ClubApplicationListResponse> findAllClubApplyByRecruitingPostId(@PathVariable Long clubId, @PathVariable Long recruitingPostId) {
        ClubApplicationListResponse allClubApplyByRecruitingPostId = clubAdminService.findAllClubApplyByRecruitingPostId(clubId, recruitingPostId);
        return ResponseEntity.ok(allClubApplyByRecruitingPostId);
    }
}
