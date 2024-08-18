package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponseWrapper;
import org.springframework.data.domain.Page;

import java.util.List;

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
