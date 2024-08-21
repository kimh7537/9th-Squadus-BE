package com.cotato.squadus.api.match.dto.matchResult.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultAddRequest {

    private Long clubMemberId;
    private Integer homeScore;
    private Integer awayScore;
}
