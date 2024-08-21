package com.cotato.squadus.api.match.dto.matchResult.response;

public record MatchFinalResultResponse(
        int homeWins,
        int awayWins
) {}