package com.cotato.squadus.api.mercenary.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record MercenaryRequestResponseWrapper(
	List<MercenaryRequestResponse> matches
) {
	public static MercenaryRequestResponseWrapper from(List<MercenaryRequestResponse> responses) {
		return new MercenaryRequestResponseWrapper(responses);
	}

	public static MercenaryRequestResponseWrapper from(Page<MercenaryRequestResponse> responses) {
		return new MercenaryRequestResponseWrapper(responses.getContent());
	}
}
