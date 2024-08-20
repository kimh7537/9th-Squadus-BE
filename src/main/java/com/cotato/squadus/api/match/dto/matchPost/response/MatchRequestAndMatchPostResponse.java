package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

import java.util.List;

public record MatchRequestAndMatchPostResponse(
        MatchCreateResponse matchCreateResponse,
        List<ReceivedMatchRequestResponse> receivedRequests
) {
    public static MatchRequestAndMatchPostResponse from(MatchPost matchPost, List<ReceivedMatchRequestResponse> receivedRequests) {
        return new MatchRequestAndMatchPostResponse(
                MatchCreateResponse.from(matchPost),
                receivedRequests
        );
    }

}
