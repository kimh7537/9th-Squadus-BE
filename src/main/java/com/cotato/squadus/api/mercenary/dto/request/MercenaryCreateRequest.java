package com.cotato.squadus.api.mercenary.dto.request;

import com.cotato.squadus.api.match.dto.matchPost.request.MatchCreateRequest;
import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MercenaryCreateRequest {
    private Long homeClubId;
    private Long clubMemberId;
    private String title;
    private String content;
    private MatchPlaceRequest matchPlace;
    private Boolean placeProvided;
    private LocalDate matchStartDate;
    private LocalTime matchStartTime;
    private Integer maxParticipants;

    @Getter
    @Setter
    public static class MatchPlaceRequest {
        private String city;
        private String district;
    }
}
