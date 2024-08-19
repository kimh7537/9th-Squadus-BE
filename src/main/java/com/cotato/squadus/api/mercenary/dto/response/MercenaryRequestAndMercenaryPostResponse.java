package com.cotato.squadus.api.mercenary.dto.response;

import com.cotato.squadus.api.match.dto.matchPost.response.MatchCreateResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.MatchRequestAndMatchPostResponse;
import com.cotato.squadus.api.match.dto.matchPost.response.ReceivedMatchRequestResponse;
import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import com.cotato.squadus.domain.club.match.entity.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.MercenaryRequest;
import com.cotato.squadus.domain.club.match.enums.MatchingStatus;

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
