package com.cotato.squadus.api.fee.dto;

import com.cotato.squadus.domain.club.fee.entity.FeeType;
import com.cotato.squadus.domain.club.fee.enums.FeeCategory;

import java.time.LocalDate;

public record ClubFeeSummaryResponse(
        Long feeTypeId,
        String feeTypeName,
        FeeCategory feeCategory,
        Long price,
        Long balance,
        LocalDate endDate
) {

    public static ClubFeeSummaryResponse from(FeeType feeType) {
        return new ClubFeeSummaryResponse(
                feeType.getFeeTypeId(),
                feeType.getFeeTypeName(),
                feeType.getFeeCategory(),
                feeType.getPrice(),
                feeType.getBalance(),
                feeType.getEndDate()
        );
    }
}
