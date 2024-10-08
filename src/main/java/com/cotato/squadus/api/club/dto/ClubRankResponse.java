package com.cotato.squadus.api.club.dto;

public record ClubRankResponse(
	String logoUrl,        // 동아리 로고 URL
	String clubName,       // 동아리 이름
	int matchScore,        // 동아리 점수
	int currentRank,       // 현재 순위
	int rankChange         // 순위 변동 (이전 순위 - 현재 순위)
) {
}