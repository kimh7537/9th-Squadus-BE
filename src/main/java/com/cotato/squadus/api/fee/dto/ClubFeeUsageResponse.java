package com.cotato.squadus.api.fee.dto;

import com.cotato.squadus.domain.club.fee.entity.FeeUsage;

import java.time.LocalDate;

public record ClubFeeUsageResponse(
        Long feeUsageId,
        LocalDate usedAt,
        String description,
        Long price
) {

    public static ClubFeeUsageResponse from(FeeUsage feeUsage) {
        return new ClubFeeUsageResponse(
                feeUsage.getFeeUsageId(),
                feeUsage.getUsedAt(),
                feeUsage.getDescription(),
                feeUsage.getPrice()
        );
    }
}
