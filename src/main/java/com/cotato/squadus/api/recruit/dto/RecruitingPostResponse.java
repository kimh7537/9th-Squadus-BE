package com.cotato.squadus.api.recruit.dto;

import com.cotato.squadus.domain.club.common.entity.Region;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;

import java.time.LocalDate;
import java.util.List;

public record RecruitingPostResponse(
        Long postId,
        Long clubId,
        Boolean isActive,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Region region,
        ClubTier clubTier,
        List<String> tags
) {

    public static RecruitingPostResponse from(RecruitingPost recruitingPost) {
        return new RecruitingPostResponse(
                recruitingPost.getPostId(),
                recruitingPost.getClub().getClubId(),
                recruitingPost.getIsActive(),
                recruitingPost.getTitle(),
                recruitingPost.getStartDate(),
                recruitingPost.getEndDate(),
                recruitingPost.getClub().getRegion(),
                recruitingPost.getClub().getClubTier(),
                recruitingPost.getClub().getTags()
        );
    }
}
