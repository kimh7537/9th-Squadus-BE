package com.cotato.squadus.api.match.dto.match.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record MatchCreateResponseWrapper(
        List<MatchCreateResponse> matches
) {
    public static MatchCreateResponseWrapper from(List<MatchCreateResponse> responses) {
        return new MatchCreateResponseWrapper(responses);
    }

    public static MatchCreateResponseWrapper from(Page<MatchCreateResponse> responses) {
        return new MatchCreateResponseWrapper(responses.getContent());
    }
}
