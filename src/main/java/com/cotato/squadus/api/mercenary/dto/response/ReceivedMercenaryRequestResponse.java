package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record ReceivedMercenaryRequestResponse(
        String matchingStatus,
        Long requestId,
        Long memberId,
        String requestName,
        String requesterUniversity
) {
    public static ReceivedMercenaryRequestResponse from(MercenaryRequest mercenaryRequest) {
        return new ReceivedMercenaryRequestResponse(
                mercenaryRequest.getStatus().name(),
                mercenaryRequest.getMercenaryRequestIdx(),
                mercenaryRequest.getMember().getMemberIdx(),
                mercenaryRequest.getMember().getUsername(),   // 요청자 이름
                mercenaryRequest.getMember().getUniversity()  // 요청자 소속 대학
        );
    }
}