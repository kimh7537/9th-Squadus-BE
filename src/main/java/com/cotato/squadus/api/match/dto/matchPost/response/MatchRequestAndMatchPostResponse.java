package com.cotato.squadus.api.match.dto.matchPost.response;

import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

import java.util.List;

public record MatchRequestAndMatchPostResponse(
        Long matchRequestIdx,
        String clubName,
        MatchingStatus status,
        MatchCreateResponse matchCreateResponse,
        List<ReceivedMatchRequestResponse> receivedRequests
) {
    public static MatchRequestAndMatchPostResponse from(MatchRequest matchRequest, List<ReceivedMatchRequestResponse> receivedRequests) {
        return new MatchRequestAndMatchPostResponse(
                matchRequest.getMatchRequestIdx(),
                matchRequest.getClub().getClubName(),  //쿼리 1개 발생
                matchRequest.getStatus(),
                MatchCreateResponse.from(matchRequest.getMatchPost()),
                receivedRequests
        );
    }

}
