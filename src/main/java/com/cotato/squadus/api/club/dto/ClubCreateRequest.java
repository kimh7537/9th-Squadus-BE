package com.cotato.squadus.api.club.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateRequest {

    private String clubName;

    private String university;

    private String sportsCategory;

    private String logo;
}
