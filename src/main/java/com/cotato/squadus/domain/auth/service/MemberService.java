package com.cotato.squadus.domain.auth.service;

import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public MemberInfoResponse findMemberInfo(String accessToken) {
        String uniqueId = jwtUtil.getUniqueId(accessToken);
        Member findMember = memberRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 회원을 찾을 수 없습니다."));
        return MemberInfoResponse.from(findMember);
    }
}
