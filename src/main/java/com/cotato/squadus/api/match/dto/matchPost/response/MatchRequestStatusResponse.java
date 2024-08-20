package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record MatchRequestStatusResponse(
        Long matchRequestId,
        String clubName,
        String university,
        MatchingStatus status
) {
    public static MatchRequestStatusResponse from(MatchRequest matchRequest) {
        return new MatchRequestStatusResponse(
                matchRequest.getMatchRequestIdx(),
                matchRequest.getClub().getClubName(), //쿼리 발생
                matchRequest.getClub().getUniversity(), //쿼리 발생
                matchRequest.getStatus()
        );
    }
}