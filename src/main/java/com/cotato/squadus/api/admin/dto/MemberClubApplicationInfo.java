package com.cotato.squadus.api.admin.dto;

import com.cotato.squadus.domain.club.common.entity.ClubApplication;

public record MemberClubApplicationInfo(
	Long memberIdx,
	String username,
	String university
) {
	public static MemberClubApplicationInfo from(ClubApplication clubApplication) {
		return new MemberClubApplicationInfo(
			clubApplication.getMember().getMemberIdx(),
			clubApplication.getMember().getUsername(),
			clubApplication.getMember().getUniversity()
		);
	}

}
