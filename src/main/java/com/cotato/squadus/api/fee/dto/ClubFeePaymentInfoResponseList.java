package com.cotato.squadus.api.fee.dto;

import java.util.List;

public record ClubFeePaymentInfoResponseList(
        List<ClubFeePaymentInfoResponse> clubFeePaymentInfoResponseList
) {
    public static ClubFeePaymentInfoResponseList from(List<ClubFeePaymentInfoResponse> clubFeePaymentInfoResponseList) {
        return new ClubFeePaymentInfoResponseList(clubFeePaymentInfoResponseList);
    }
}
