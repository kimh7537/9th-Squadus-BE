package com.cotato.squadus.api.fee.dto;

import java.util.Map;

public record ClubFeePaymentUpdateRequest(
	Map<Long, Boolean> paymentsInfo
) {
}
