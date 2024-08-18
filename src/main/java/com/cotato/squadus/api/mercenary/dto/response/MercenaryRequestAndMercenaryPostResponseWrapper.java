package com.cotato.squadus.api.mercenary.dto.response;

import java.util.List;

public record MercenaryRequestAndMercenaryPostResponseWrapper(
        List<MercenaryRequestAndMercenaryPostResponse> responses
) {
    public static MercenaryRequestAndMercenaryPostResponseWrapper from(List<MercenaryRequestAndMercenaryPostResponse> responses) {
        return new MercenaryRequestAndMercenaryPostResponseWrapper(responses);
    }
}
