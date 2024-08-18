package com.cotato.squadus.api.match.dto.matchPost.request;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterRequest {

    private String sportsCategory;
    private String city;
    private String district;
    private String tier;
    private Boolean placeProvided;
}
