package com.cotato.squadus.api.match.dto.matchPost.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record MatchRequestResponseWrapper(
        List<MatchRequestResponse> matches
) {
    public static MatchRequestResponseWrapper from(List<MatchRequestResponse> responses) {
        return new MatchRequestResponseWrapper(responses);
    }

    public static MatchRequestResponseWrapper from(Page<MatchRequestResponse> responses) {
        return new MatchRequestResponseWrapper(responses.getContent());
    }
}
