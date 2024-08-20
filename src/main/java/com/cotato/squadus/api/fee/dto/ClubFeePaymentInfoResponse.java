package com.cotato.squadus.api.fee.dto;

import com.cotato.squadus.domain.club.fee.entity.FeePayment;

public record ClubFeePaymentInfoResponse(
        Long clubMemberIdx,
        String name,
        String profileImage,
        Boolean isPaid,
        Boolean isUser
) {
    public static ClubFeePaymentInfoResponse from(FeePayment feePayment, Boolean isUser) {
        return new ClubFeePaymentInfoResponse(
                feePayment.getClubMember().getClubMemberIdx(),
                feePayment.getClubMember().getMember().getUsername(),
                feePayment.getClubMember().getMember().getProfileImage(),
                feePayment.getIsPaid(),
                isUser
        );
    }
}
