package com.cotato.squadus.api.fee.dto;

import java.util.List;

public record ClubFeeSummaryResponseList(
	Long totalBalance,
	List<ClubFeeSummaryResponse> clubFeeSummaryResponseList
) {

	public static ClubFeeSummaryResponseList from(Long totalBalance,
		List<ClubFeeSummaryResponse> clubFeeSummaryResponseList) {
		return new ClubFeeSummaryResponseList(totalBalance, clubFeeSummaryResponseList);
	}
}
