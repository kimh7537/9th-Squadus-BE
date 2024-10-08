package com.cotato.squadus.api.fee.dto;

import java.time.LocalDate;

import com.cotato.squadus.domain.club.fee.entity.FeeUsage;

public record ClubFeeUsageResponse(
	Long feeTypeId,
	Long feeUsageId,
	LocalDate usedAt,
	String description,
	Long price
) {

	public static ClubFeeUsageResponse from(FeeUsage feeUsage) {
		return new ClubFeeUsageResponse(
			feeUsage.getFeeType().getFeeTypeId(),
			feeUsage.getFeeUsageId(),
			feeUsage.getUsedAt(),
			feeUsage.getDescription(),
			feeUsage.getPrice()
		);
	}
}
