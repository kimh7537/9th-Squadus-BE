package com.cotato.squadus.api.fee.dto;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.fee.enums.FeeCategory;

import java.time.LocalDate;
import java.util.List;

public record ClubFeeCreateRequest(
        String feeTypeName,
        Long price,
        FeeCategory feeCategory,
        LocalDate startDate,
        LocalDate endDate,
        List<Long> clubMemberIds,
        String memo
) {
}
