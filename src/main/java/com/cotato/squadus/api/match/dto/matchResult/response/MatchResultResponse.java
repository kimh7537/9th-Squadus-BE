package com.cotato.squadus.api.match.dto.matchResult.response;

import com.cotato.squadus.domain.club.match.entity.match.MatchResult;

public record MatchResultResponse(
        Long matchResultIdx,
        Long matchPostId,
        String homeClubName,
        String awayClubName,
        Integer homeScore,
        Integer awayScore,
        Boolean isFinalizedHome,
        Boolean isFinalizedAway
) {
    public static MatchResultResponse from(MatchResult matchResult) {
        return new MatchResultResponse(
                matchResult.getMatchResultIdx(),
                matchResult.getMatchPost().getMatchIdx(),
                matchResult.getHomeClub().getClubName(),
                matchResult.getAwayClub().getClubName(),
                matchResult.getHomeScore(),
                matchResult.getAwayScore(),
                matchResult.getIsFinalizedHome(),
                matchResult.getIsfinalizedAway()
        );
    }
}
