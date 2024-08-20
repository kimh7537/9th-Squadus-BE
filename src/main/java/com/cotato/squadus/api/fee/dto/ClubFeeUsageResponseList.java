package com.cotato.squadus.api.fee.dto;

import java.util.List;

public record ClubFeeUsageResponseList(
        List<ClubFeeUsageResponse> clubFeeUsageResponseList
) {
    public static ClubFeeUsageResponseList from(List<ClubFeeUsageResponse> clubFeeUsageResponseList) {
        return new ClubFeeUsageResponseList(clubFeeUsageResponseList);
    }
}
