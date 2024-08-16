package com.cotato.squadus.api.match.dto.match.request;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchFilterRequest {

    private SportsCategory sportsCategory;
    private String city;
    private String district;
    private Tier tier;
    private Boolean placeProvided;
}
