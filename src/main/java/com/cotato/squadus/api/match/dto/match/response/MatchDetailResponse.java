package com.cotato.squadus.api.match.dto.match.response;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.MatchPost;

public record MatchDetailResponse(
        String homeClubName,
        String homeClubLogoUrl,
        String homeClubTier,
        String awayClubName,
        String awayClubLogoUrl,
        String awayClubTier,
        String matchTitle,
        String matchContent
) {
    public static MatchDetailResponse from(MatchPost matchPost, Club awayClub) {
        return new MatchDetailResponse(
                matchPost.getHomeClub().getClubName(),
                matchPost.getHomeClub().getLogo(),
                matchPost.getHomeClub().getClubTier().name(),
                awayClub.getClubName(),
                awayClub.getLogo(),
                awayClub.getClubTier().name(),
                matchPost.getTitle(),
                matchPost.getContent()
        );
    }
}

