package com.cotato.squadus.domain.auth.service;

import com.cotato.squadus.api.member.dto.MemberClubListResponse;
import com.cotato.squadus.api.member.dto.MemberClubResponse;
import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public Long saveMember(Member member) {
        Member save = memberRepository.save(member);
        return save.getMemberIdx();
    }

    public MemberInfoResponse findMemberInfo(CustomOAuth2Member customOAuth2Member) {
        Member findMember = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 회원을 찾을 수 없습니다."));
        return MemberInfoResponse.from(findMember);
    }

    public Member findMemberByUniqueId(String uniqueId) {
        Member member = memberRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));
        return member;
    }

    public MemberClubListResponse findJoinedClubs(CustomOAuth2Member customOAuth2Member) {
        Member member = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
                .orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));
        List<ClubMember> clubMemberships = member.getClubMemberships();
        List<MemberClubResponse> memberClubResponseList = clubMemberships.stream()
                .map(MemberClubResponse::from)
                .toList();

        return MemberClubListResponse.from(memberClubResponseList);
    }
}
