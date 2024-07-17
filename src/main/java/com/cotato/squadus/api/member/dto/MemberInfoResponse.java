package com.cotato.squadus.api.member.dto;

import com.cotato.squadus.domain.auth.entity.Member;

public record MemberInfoResponse(
        Long memberId,
        String memberName,
        String email,
        String memberRole,
        String university
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getMemberIdx(),
                member.getUsername(),
                member.getEmail(),
                member.getMemberRole().name(),
                member.getUniversity()
        );
    }
}