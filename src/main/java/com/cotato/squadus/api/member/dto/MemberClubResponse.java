package com.cotato.squadus.api.member.dto;

import com.cotato.squadus.domain.club.common.entity.ClubMember;

public record MemberClubResponse(
        Long clubId,
        String clubName
) {

    public static MemberClubResponse from(ClubMember clubMember) {
        return new MemberClubResponse(
                clubMember.getClub().getClubId(),
                clubMember.getClub().getClubName());
    }
}
