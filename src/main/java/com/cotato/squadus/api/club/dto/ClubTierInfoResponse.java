package com.cotato.squadus.api.club.dto;

public record ClubTierInfoResponse(
	String clubTier,
	int teamCount,  //같은 종목을 가진 club의 갯수
	int currentRank,
	int ranksToNextTier
) {

}
