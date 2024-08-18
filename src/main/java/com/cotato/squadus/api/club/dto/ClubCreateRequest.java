package com.cotato.squadus.api.club.dto;

import com.cotato.squadus.domain.club.common.entity.Region;
import com.cotato.squadus.domain.club.common.enums.ClubCategory;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateRequest {

    private String clubName;

    private ClubCategory clubCategory;

    private SportsCategory sportsCategory;

    private Long maxMembers;

    private String city;

    private String district;
}
