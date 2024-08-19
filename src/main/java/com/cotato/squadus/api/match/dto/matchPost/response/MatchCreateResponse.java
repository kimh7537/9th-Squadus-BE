package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.MatchPost;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchCreateResponse(
        Long matchIdx,
        String title,
        SportsCategory sportsCategory,
        String content,
        Tier tier,
        MatchPlaceResponse matchPlace,
        Boolean placeProvided,
        LocalDate matchStartDate,
        LocalTime matchStartTime,
        Integer currentParticipants,
        Integer maxParticipants
) {
    public static MatchCreateResponse from(MatchPost matchPost) {
        return new MatchCreateResponse(
                matchPost.getMatchIdx(),
                matchPost.getTitle(),
                matchPost.getSportsCategory(),
                matchPost.getContent(),
                matchPost.getTier(),
                MatchPlaceResponse.from(matchPost.getMatchPlace()),
                matchPost.getPlaceProvided(),
                matchPost.getMatchStartDate(),
                matchPost.getMatchStartTime(),
                matchPost.getCurrentParticipants(),
                matchPost.getMaxParticipants()
        );
    }
}
