package com.cotato.squadus.api.member.controller;

import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.auth.service.MemberService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController()
@RequestMapping("/v1/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @GetMapping("/info")
    public MemberInfoResponse findMemberInfo(
            @RequestHeader("access") String accessToken) {
        MemberInfoResponse memberInfo = memberService.findMemberInfo(accessToken);
        return memberInfo;
    }
}
