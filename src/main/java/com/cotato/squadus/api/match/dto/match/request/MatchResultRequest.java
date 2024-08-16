package com.cotato.squadus.api.match.dto.match.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultRequest {
    private Long matchPostId;
    private Integer homeScore;
    private Integer awayScore;
}
