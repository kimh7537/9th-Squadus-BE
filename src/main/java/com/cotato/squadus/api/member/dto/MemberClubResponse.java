package com.cotato.squadus.api.member.dto;

import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import com.cotato.squadus.domain.club.common.entity.ClubMember;

public record MemberClubResponse(
        Long clubId,
        String clubName,
        Boolean isAdmin
) {

    public static MemberClubResponse from(ClubMember clubMember) {
        boolean isAdmin = false;

        if (clubMember instanceof ClubAdminMember) {
            ClubAdminMember adminMember = (ClubAdminMember) clubMember;
            isAdmin = adminMember.getAdminStatus() == AdminStatus.CURRENT;
        }

        return new MemberClubResponse(
                clubMember.getClub().getClubId(),
                clubMember.getClub().getClubName(),
                isAdmin
        );
    }
}