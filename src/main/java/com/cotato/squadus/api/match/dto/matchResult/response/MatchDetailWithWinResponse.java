package com.cotato.squadus.api.match.dto.matchResult.response;

public record MatchDetailWithWinResponse(
	MatchDetailResponse matchDetail,
	MatchFinalResultResponse matchFinalResult
) {
}
