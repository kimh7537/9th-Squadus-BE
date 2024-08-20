package com.cotato.squadus.api.club.dto;

import java.util.List;

public record ClubMemberInfoResponseList(
        List<ClubMemberInfoResponse> clubMemberInfoResponseList
) {
    public static ClubMemberInfoResponseList from(List<ClubMemberInfoResponse> clubMemberInfoResponseList) {
        return new ClubMemberInfoResponseList(clubMemberInfoResponseList);
    }
}
