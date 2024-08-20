package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record MercenaryRequestResponse(
        Long mercenaryRequestIdx,
        String clubName, //신청한 ClubMember의 동아리 이름
        MatchingStatus status,
        MercenaryCreateResponse mercenaryCreateResponse
) {
    public static MercenaryRequestResponse from(MercenaryRequest mercenaryRequest) {
        return new MercenaryRequestResponse(
                mercenaryRequest.getMercenaryRequestIdx(),
                mercenaryRequest.getClubMember().getClub().getClubName(),  //쿼리 1개 발생
                mercenaryRequest.getStatus(),
                MercenaryCreateResponse.from(mercenaryRequest.getMercenaryPost())
        );
    }
}
