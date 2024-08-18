package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.match.entity.MercenaryRequest;

public record ReceivedMercenaryRequestResponse(
        Long requestId,
        String requestName,
        String requesterUniversity
) {
    public static ReceivedMercenaryRequestResponse from(MercenaryRequest mercenaryRequest) {
        return new ReceivedMercenaryRequestResponse(
                mercenaryRequest.getMercenaryRequestIdx(),
                mercenaryRequest.getClubMember().getMember().getUsername(),   // 요청자 이름
                mercenaryRequest.getClubMember().getMember().getUniversity()  // 요청자 소속 대학
        );
    }
}