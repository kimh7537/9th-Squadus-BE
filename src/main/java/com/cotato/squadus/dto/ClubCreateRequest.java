package com.cotato.squadus.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
