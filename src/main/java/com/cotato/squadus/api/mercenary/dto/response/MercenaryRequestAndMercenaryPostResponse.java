package com.cotato.squadus.api.mercenary.dto.response;

import java.util.List;

import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;

public record MercenaryRequestAndMercenaryPostResponse(
	MercenaryCreateResponse mercenaryCreateResponse,
	List<ReceivedMercenaryRequestResponse> receivedRequests
) {
	public static MercenaryRequestAndMercenaryPostResponse from(MercenaryPost mercenaryPost,
		List<ReceivedMercenaryRequestResponse> receivedRequests) {
		return new MercenaryRequestAndMercenaryPostResponse(
			MercenaryCreateResponse.from(mercenaryPost),
			receivedRequests
		);
	}

}
