package com.cotato.squadus.api.match.dto.matchPost.response;

import java.util.List;

public record MatchRequestAndMatchPostResponseWrapper(
        List<MatchRequestAndMatchPostResponse> matchRequestAndMatchPostResponses
) {
    public static MatchRequestAndMatchPostResponseWrapper from(List<MatchRequestAndMatchPostResponse> responses) {
        return new MatchRequestAndMatchPostResponseWrapper(responses);
    }
}
