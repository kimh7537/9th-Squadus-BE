package com.cotato.squadus.api.club.dto;

import com.cotato.squadus.domain.club.common.entity.ClubMember;

public record ClubMemberInfoResponse(
        Long id,
        String name,
        String profileImg
) {
    public static ClubMemberInfoResponse from(ClubMember clubMember) {
        return new ClubMemberInfoResponse(
                clubMember.getClubMemberIdx(),
                clubMember.getMember().getUsername(),
                clubMember.getMember().getProfileImage()
        );
    }

}
