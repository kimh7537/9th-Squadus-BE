package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.match.entity.match.MatchPost;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreateResponse(
        Long matchIdx,
        String title,
        String content,
        String tier,
        MatchPlaceResponse matchPlace,
        Boolean placeProvided,
        LocalDate matchStartDate,
        LocalTime matchStartTime,
        Integer maxParticipants,
        Long clubIdx,
        String sportsCategory,
        String clubName,
        String clubLogo
) {
    public static MatchCreateResponse from(MatchPost matchPost) {
        return new MatchCreateResponse(
                matchPost.getMatchIdx(),
                matchPost.getTitle(),
                matchPost.getContent(),
                matchPost.getTier().name(),
                MatchPlaceResponse.from(matchPost.getMatchPlace()),
                matchPost.getPlaceProvided(),
                matchPost.getMatchStartDate(),
                matchPost.getMatchStartTime(),
                matchPost.getMaxParticipants(),
                matchPost.getHomeClub().getClubId(),
                matchPost.getHomeClub().getSportsCategory().name(),
                matchPost.getHomeClub().getClubName(),
                matchPost.getHomeClub().getLogo()
        );
    }
}
