package com.cotato.squadus.api.fee.dto;

import java.util.List;

public record ClubFeeUsageResponseList(
	List<DateGroupedClubFeeUsageResponse> dateGroupedClubFeeUsageResponseList
) {
	public static ClubFeeUsageResponseList from(
		List<DateGroupedClubFeeUsageResponse> dateGroupedClubFeeUsageResponseList) {
		return new ClubFeeUsageResponseList(dateGroupedClubFeeUsageResponseList);
	}
}
