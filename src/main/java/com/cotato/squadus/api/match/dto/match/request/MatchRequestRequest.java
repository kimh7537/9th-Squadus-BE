package com.cotato.squadus.api.match.dto.match.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchRequestRequest {
    private Long memberId;
    private Long clubId;
    private Long matchPostId;
}