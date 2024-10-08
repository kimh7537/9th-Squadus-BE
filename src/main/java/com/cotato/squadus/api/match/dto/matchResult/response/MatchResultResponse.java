package com.cotato.squadus.api.match.dto.matchResult.response;

import com.cotato.squadus.domain.club.match.entity.match.MatchResult;

public record MatchResultResponse(
	Long matchResultIdx,
	Long matchPostId,
	Integer homeScore,
	Integer awayScore
) {
	public static MatchResultResponse from(MatchResult matchResult) {
		return new MatchResultResponse(
			matchResult.getMatchResultIdx(),
			matchResult.getMatchPost().getMatchIdx(), //option으로 넣음
			matchResult.getHomeScore(),
			matchResult.getAwayScore()
		);
	}
}
