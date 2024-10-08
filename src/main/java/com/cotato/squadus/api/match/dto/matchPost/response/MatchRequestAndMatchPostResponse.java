package com.cotato.squadus.api.match.dto.matchPost.response;

import java.util.List;

import com.cotato.squadus.domain.club.match.entity.match.MatchPost;

public record MatchRequestAndMatchPostResponse(
	MatchCreateResponse matchCreateResponse,
	List<ReceivedMatchRequestResponse> receivedRequests
) {
	public static MatchRequestAndMatchPostResponse from(MatchPost matchPost,
		List<ReceivedMatchRequestResponse> receivedRequests) {
		return new MatchRequestAndMatchPostResponse(
			MatchCreateResponse.from(matchPost),
			receivedRequests
		);
	}

}
