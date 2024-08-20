package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

public record ReceivedMatchRequestResponse(
        String matchingStatus,
        Long requestId,
        String requesterClubName,
        String requesterUniversity,
        String requesterTier
) {
    public static ReceivedMatchRequestResponse from(MatchRequest matchRequest) {
        return new ReceivedMatchRequestResponse(
                matchRequest.getStatus().name(),
                matchRequest.getMatchRequestIdx(),
                matchRequest.getClub().getClubName(),
                matchRequest.getClub().getUniversity(),
                matchRequest.getClub().getClubTier().name()
        );
    }
}