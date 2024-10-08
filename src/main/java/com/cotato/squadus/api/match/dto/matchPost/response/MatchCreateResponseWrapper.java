package com.cotato.squadus.api.match.dto.matchPost.response;

import java.util.List;

import org.springframework.data.domain.Page;

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
