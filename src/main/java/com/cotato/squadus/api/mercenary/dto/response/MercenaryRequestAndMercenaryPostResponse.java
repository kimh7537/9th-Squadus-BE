package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;

import java.util.List;

public record MercenaryRequestAndMercenaryPostResponse(
        MercenaryCreateResponse mercenaryCreateResponse,
        List<ReceivedMercenaryRequestResponse> receivedRequests
) {
    public static MercenaryRequestAndMercenaryPostResponse from(MercenaryPost mercenaryPost, List<ReceivedMercenaryRequestResponse> receivedRequests) {
        return new MercenaryRequestAndMercenaryPostResponse(
                MercenaryCreateResponse.from(mercenaryPost),
                receivedRequests
        );
    }

}
