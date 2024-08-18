package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.match.entity.MatchPlace;

public record MatchPlaceResponse(
        String city,
        String district
) {
    public static MatchPlaceResponse from(MatchPlace matchPlace) {
        return new MatchPlaceResponse(
                matchPlace.getCity(),
                matchPlace.getDistrict()
        );
    }
}
