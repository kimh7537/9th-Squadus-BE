package com.cotato.squadus.api.fee.dto;

import java.time.LocalDate;
import java.util.List;

public record DateGroupedClubFeeUsageResponse(
        LocalDate date,
        List<ClubFeeUsageResponse> usages
) {
}
