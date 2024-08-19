package com.cotato.squadus.api.fee.dto;

import java.time.LocalDate;

public record ClubFeeUsageRequest(
        String description,
        LocalDate usedAt,
        Long price
) {
}
