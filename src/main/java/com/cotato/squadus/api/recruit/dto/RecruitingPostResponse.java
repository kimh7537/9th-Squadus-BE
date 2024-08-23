package com.cotato.squadus.api.recruit.dto;

import com.cotato.squadus.domain.club.common.entity.Region;
import com.cotato.squadus.domain.club.common.enums.ClubCategory;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;

import java.time.LocalDate;
import java.util.List;

public record RecruitingPostResponse(
        Long recruitingPostId,
        Long clubId,
        String clubName,
        Boolean isActive,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        SportsCategory sportsCategory,
        ClubCategory clubCategory,
        Region region,
        ClubTier clubTier,
        List<String> tags
) {

    public static RecruitingPostResponse from(RecruitingPost recruitingPost) {
        return new RecruitingPostResponse(
                recruitingPost.getPostId(),
                recruitingPost.getClub().getClubId(),
                recruitingPost.getClub().getClubName(),
                recruitingPost.getIsActive(),
                recruitingPost.getTitle(),
                recruitingPost.getStartDate(),
                recruitingPost.getEndDate(),
                recruitingPost.getClub().getSportsCategory(),
                recruitingPost.getClub().getClubCategory(),
                recruitingPost.getClub().getRegion(),
                recruitingPost.getClub().getClubTier(),
                recruitingPost.getClub().getTags()
        );
    }
}
