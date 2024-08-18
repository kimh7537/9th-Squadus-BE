package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestResponseWrapper;
import org.springframework.data.domain.Page;

import java.util.List;

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
