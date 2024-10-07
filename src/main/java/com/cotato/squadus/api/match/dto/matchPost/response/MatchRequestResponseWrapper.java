package com.cotato.squadus.api.match.dto.matchPost.response;

import java.util.List;

import org.springframework.data.domain.Page;

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
