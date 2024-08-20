package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record MercenaryRequestStatusResponse(
        Long mercenaryRequestId,
        String clubName,
        String university,
        MatchingStatus status
) {
    public static MercenaryRequestStatusResponse from(MercenaryRequest mercenaryRequest) {
        return new MercenaryRequestStatusResponse(
                mercenaryRequest.getMercenaryRequestIdx(),
                mercenaryRequest.getClubMember().getClub().getClubName(), //쿼리 발생
                mercenaryRequest.getClubMember().getMember().getUniversity(), //쿼리 발생
                mercenaryRequest.getStatus()
        );
    }
}