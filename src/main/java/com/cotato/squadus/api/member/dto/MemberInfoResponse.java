package com.cotato.squadus.api.member.dto;

import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.enums.MemberRole;

public record MemberInfoResponse(
	Long memberId,
	String memberName,
	String email,
	MemberRole memberRole,
	String university,
	String profileImage
) {
	public static MemberInfoResponse from(Member member) {
		return new MemberInfoResponse(
			member.getMemberIdx(),
			member.getUsername(),
			member.getEmail(),
			member.getMemberRole(),
			member.getUniversity(),
			member.getProfileImage()
		);
	}
}