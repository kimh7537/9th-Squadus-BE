package com.cotato.squadus.api.admin.dto;

import com.cotato.squadus.domain.club.common.entity.ClubApplication;

public record ClubApplicationInfoResponse(
	Long applicationId,
	Long recruitingPostId,
	MemberClubApplicationInfo memberClubApplicationInfo
) {
	public static ClubApplicationInfoResponse from(ClubApplication clubApplication) {
		return new ClubApplicationInfoResponse(
			clubApplication.getApplicationIdx(),
			clubApplication.getRecruitingPost().getPostId(),
			MemberClubApplicationInfo.from(clubApplication)
		);
	}
}
