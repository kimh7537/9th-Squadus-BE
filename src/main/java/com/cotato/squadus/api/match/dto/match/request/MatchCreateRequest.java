package com.cotato.squadus.api.match.dto.match.request;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MatchCreateRequest {
    private Long homeClubId;
    private Long memberId;
    private SportsCategory sportsCategory;
    private String title;
    private String content;
    private Tier tier;
    private MatchPlaceRequest matchPlace;
    private Boolean placeProvided;
    private LocalDate matchStartDate;
    private LocalTime matchStartTime;
    private Integer currentParticipants;
    private Integer maxParticipants;


    @Getter
    @Setter
    public static class MatchPlaceRequest {
        private String city;
        private String district;
    }
}