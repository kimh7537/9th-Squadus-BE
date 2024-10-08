package com.cotato.squadus.api.fee.dto;

import java.time.LocalDate;
import java.util.List;

import com.cotato.squadus.domain.club.fee.enums.FeeCategory;

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
