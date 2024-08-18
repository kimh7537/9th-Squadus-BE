package com.cotato.squadus.api.match.dto.matchPost.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResultRequest {
    private Long matchPostId;
    private Integer homeScore;
    private Integer awayScore;
}
