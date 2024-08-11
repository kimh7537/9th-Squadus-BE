package com.cotato.squadus.api.member.controller;

import com.cotato.squadus.api.member.dto.MemberClubListResponse;
import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "유저", description = "유저 관련 API")
@Slf4j
@RestController()
@RequestMapping("/v1/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @GetMapping("/info")
    @Operation(summary = "유저 정보 조회", description = "Access Token을 통해 유저에 대한 정보를 조회합니다")
    public MemberInfoResponse findMemberInfo(
            @RequestHeader("access") String accessToken) {
        MemberInfoResponse memberInfo = memberService.findMemberInfo(accessToken);
        return memberInfo;
    }

    @GetMapping("/clubs")
    public ResponseEntity<MemberClubListResponse> findJoinedClubs(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        MemberClubListResponse memberClubListResponse = memberService.findJoinedClubs(customOAuth2Member);
        return ResponseEntity.ok(memberClubListResponse);
    }

}
