package com.cotato.squadus.api.match.dto.matchPost.request;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MatchCreateRequest {
    private Long homeClubId;
    private Long clubMemberId;
    private String title;
    private String content;
    private String tier;
    private MatchPlaceRequest matchPlace;
    private Boolean placeProvided;
    private LocalDate matchStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime matchStartTime;
    private Integer maxParticipants;

    @Getter
    @Setter
    public static class MatchPlaceRequest {
        private String city;
        private String district;
    }
}