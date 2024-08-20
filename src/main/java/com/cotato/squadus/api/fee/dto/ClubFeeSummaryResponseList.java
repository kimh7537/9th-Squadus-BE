package com.cotato.squadus.api.fee.dto;

import java.util.List;

public record ClubFeeSummaryResponseList(
        List<ClubFeeSummaryResponse> clubFeeSummaryResponseList
) {

    public static ClubFeeSummaryResponseList from(List<ClubFeeSummaryResponse> clubFeeSummaryResponseList) {
        return new ClubFeeSummaryResponseList(clubFeeSummaryResponseList);
    }
}
