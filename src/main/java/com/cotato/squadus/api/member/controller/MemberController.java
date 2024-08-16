package com.cotato.squadus.api.member.controller;

import com.cotato.squadus.api.member.dto.MemberClubListResponse;
import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "유저가 가입된 동아리 조회", description = "가입된 동아리에 대한 정보를 리스트로 조회")
    public ResponseEntity<MemberClubListResponse> findJoinedClubs(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        MemberClubListResponse memberClubListResponse = memberService.findJoinedClubs(customOAuth2Member);
        return ResponseEntity.ok(memberClubListResponse);
    }

    @PostMapping(value = "/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 프로필 이미지 변경", description = "유저의 프로필 이미지를 변경합니다.")
    public ResponseEntity<MemberInfoResponse> updateMemberProfileImage(
            @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member,
            @Parameter(description = "multipart/form-data 형식의 이미지를 input으로 받습니다. 이때 key 값은 profileImage입니다.")
            @RequestPart(value = "profileImage", required = true) MultipartFile profileImageFile) {
        MemberInfoResponse memberInfoResponse = memberService.updateProfileImage(customOAuth2Member, profileImageFile);
        return ResponseEntity.ok(memberInfoResponse);
    }
}
