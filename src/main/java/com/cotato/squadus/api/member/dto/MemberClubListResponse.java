package com.cotato.squadus.api.member.dto;

import java.util.List;

public record MemberClubListResponse(
	List<MemberClubResponse> memberClubResponseList
) {
	public static MemberClubListResponse from(List<MemberClubResponse> memberClubResponseList) {
		return new MemberClubListResponse(memberClubResponseList);
	}
}
