package com.cotato.squadus.api.admin.dto;

import java.util.List;

public record ClubApplicationListResponse(
	List<ClubApplicationInfoResponse> clubApplicationInfoResponseList
) {
	public static ClubApplicationListResponse from(List<ClubApplicationInfoResponse> clubApplicationInfoResponseList) {
		return new ClubApplicationListResponse(clubApplicationInfoResponseList);
	}
}
