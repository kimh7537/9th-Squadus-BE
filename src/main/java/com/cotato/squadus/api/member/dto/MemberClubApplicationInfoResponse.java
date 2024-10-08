package com.cotato.squadus.api.member.dto;

import java.time.LocalDate;
import java.util.List;

import com.cotato.squadus.domain.auth.enums.ApplicationStatus;
import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import com.cotato.squadus.domain.club.common.entity.Region;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;

public record MemberClubApplicationInfoResponse(
	Long applicationId,
	Long recruitingPostId,
	LocalDate startDate,
	LocalDate endDate,
	String title,
	Region region,
	SportsCategory sportsCategory,
	ClubTier clubTier,
	List<String> tags,
	ApplicationStatus applicationStatus
) {
	public static MemberClubApplicationInfoResponse from(ClubApplication clubApplication) {
		return new MemberClubApplicationInfoResponse(
			clubApplication.getApplicationIdx(),
			clubApplication.getRecruitingPost().getPostId(),
			clubApplication.getRecruitingPost().getStartDate(),
			clubApplication.getRecruitingPost().getEndDate(),
			clubApplication.getRecruitingPost().getTitle(),
			clubApplication.getClub().getRegion(),
			clubApplication.getClub().getSportsCategory(),
			clubApplication.getClub().getClubTier(),
			clubApplication.getClub().getTags(),
			clubApplication.getApplicationStatus()
		);
	}

}
