package com.cotato.squadus.api.member.dto;

import java.util.List;

public record MemberClubApplicationListResponse(
        Integer totalApplicationCount,
        List<MemberClubApplicationInfoResponse> memberClubApplicationInfoResponseList
) {
    public static MemberClubApplicationListResponse from(List<MemberClubApplicationInfoResponse> memberClubApplicationInfoResponseList) {
        return new MemberClubApplicationListResponse(
                memberClubApplicationInfoResponseList.size(),
                memberClubApplicationInfoResponseList
        );
    }
}
