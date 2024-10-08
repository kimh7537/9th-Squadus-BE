package com.cotato.squadus.api.club.dto;

import java.util.List;

public record ClubUpdateRequest(
	String clubMessage,
	String city,
	String district,
	Long maxMembers,
	List<String> tags
) {

}
