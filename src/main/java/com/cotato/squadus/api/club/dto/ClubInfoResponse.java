package com.cotato.squadus.api.club.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;

public record ClubInfoResponse(
	Long id,
	String clubName,
	String university,
	String clubTier,
	Integer clubRank,
	SportsCategory sportsCategory,
	String logo,
	Integer numberOfMembers,
	Long maxMembers,
	LocalDateTime createdAt,
	String clubMessage,
	List<String> tags,
	Integer matchScore
) {
	public static ClubInfoResponse from(Club club) {
		return new ClubInfoResponse(
			club.getClubId(),
			club.getClubName(),
			club.getUniversity(),
			club.getClubTier().name(),
			club.getClubRank(),
			club.getSportsCategory(),
			club.getLogo(),
			club.getClubMembers() != null ? club.getClubMembers().size() : 0,
			club.getMaxMembers(),
			club.getCreatedAt(),
			club.getClubMessage(),
			club.getTags(),
			club.getMatchScore()
		);
	}
}
