package com.cotato.squadus.api.match.dto.match.response;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.match.entity.MatchRequest;

public record ReceivedMatchRequestResponse(
        Long requestId,
        String requesterClubName,
        String requesterUniversity,
        ClubTier requesterTier
) {
    public static ReceivedMatchRequestResponse from(MatchRequest matchRequest) {
        return new ReceivedMatchRequestResponse(
                matchRequest.getMatchRequestIdx(),
                matchRequest.getClub().getClubName(),
                matchRequest.getClub().getUniversity(),
                matchRequest.getClub().getClubTier()
        );
    }
}