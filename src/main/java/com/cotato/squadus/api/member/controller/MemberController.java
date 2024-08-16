package com.cotato.squadus.api.member.controller;

import com.cotato.squadus.api.member.dto.MemberClubListResponse;
import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "유저", description = "유저 관련 API")
@Slf4j
@RestController()
@RequestMapping("/v1/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    @Operation(summary = "유저 정보 조회", description = "Access Token을 통해 유저에 대한 정보를 조회합니다")
    public MemberInfoResponse findMemberInfo(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        MemberInfoResponse memberInfo = memberService.findMemberInfo(customOAuth2Member);
        return memberInfo;
    }

    @GetMapping("/clubs")
    public ResponseEntity<MemberClubListResponse> findJoinedClubs(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        MemberClubListResponse memberClubListResponse = memberService.findJoinedClubs(customOAuth2Member);
        return ResponseEntity.ok(memberClubListResponse);
    }

    @PostMapping(value = "/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberInfoResponse> updateMemberProfileImage(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member,
            @RequestPart(value = "profileImage", required = true) MultipartFile profileImageFile) {
        MemberInfoResponse memberInfoResponse = memberService.updateProfileImage(customOAuth2Member, profileImageFile);
        return ResponseEntity.ok(memberInfoResponse);
    }
}
