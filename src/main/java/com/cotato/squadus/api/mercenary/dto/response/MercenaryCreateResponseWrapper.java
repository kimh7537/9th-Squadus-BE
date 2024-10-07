package com.cotato.squadus.api.mercenary.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record MercenaryCreateResponseWrapper(
	List<MercenaryCreateResponse> matches
) {
	public static MercenaryCreateResponseWrapper from(List<MercenaryCreateResponse> responses) {
		return new MercenaryCreateResponseWrapper(responses);
	}

	public static MercenaryCreateResponseWrapper from(Page<MercenaryCreateResponse> responses) {
		return new MercenaryCreateResponseWrapper(responses.getContent());
	}
}
