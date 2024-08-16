package com.cotato.squadus.api.match.dto.match.response;

import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record MatchRequestResponse(
        Long matchRequestIdx,
        String clubName,
        MatchingStatus status,
        MatchCreateResponse matchCreateResponse
) {
    public static MatchRequestResponse from(MatchRequest matchRequest) {
        return new MatchRequestResponse(
                matchRequest.getMatchRequestIdx(),
                matchRequest.getClub().getClubName(),  //쿼리 1개 발생
                matchRequest.getStatus(),
                MatchCreateResponse.from(matchRequest.getMatchPost())
        );
    }
}
