package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.api.match.dto.matchPost.response.MatchPlaceResponse;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;

import java.time.LocalDate;
import java.time.LocalTime;

public record MercenaryCreateResponse(
        Long mercenaryIdx,
        String title,
        String content,
        MatchPlaceResponse matchPlace,
        Boolean placeProvided,
        LocalDate matchStartDate,
        LocalTime matchStartTime,
        Integer maxParticipants,
        Integer currentParticipants,
        Long clubIdx,
        String sportsCategory,
        String clubName,
        String clubLogo
) {
    public static MercenaryCreateResponse from(MercenaryPost mercenaryPost) {
        return new MercenaryCreateResponse(
                mercenaryPost.getMercenaryIdx(),
                mercenaryPost.getTitle(),
                mercenaryPost.getContent(),
                MatchPlaceResponse.from(mercenaryPost.getMatchPlace()),
                mercenaryPost.getPlaceProvided(),
                mercenaryPost.getMatchStartDate(),
                mercenaryPost.getMatchStartTime(),
                mercenaryPost.getMaxParticipants(),
                mercenaryPost.getCurrentParticipants(),
                mercenaryPost.getHomeClub().getClubId(),
                mercenaryPost.getHomeClub().getSportsCategory().name(),
                mercenaryPost.getHomeClub().getClubName(),
                mercenaryPost.getHomeClub().getLogo()
        );
    }
}
