package com.cotato.squadus.api.club.dto;

import com.cotato.squadus.domain.club.common.enums.ClubCategory;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateRequest {

    private String clubName;

    private String university;

    private ClubCategory clubCategory;

    private SportsCategory sportsCategory;

    private String logo;

    private Long maxMembers;
}
