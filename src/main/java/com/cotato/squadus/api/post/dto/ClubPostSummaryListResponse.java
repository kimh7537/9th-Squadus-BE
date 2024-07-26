package com.cotato.squadus.api.post.dto;

import java.util.List;

public record ClubPostSummaryListResponse(
        List<ClubPostSummaryResponse> clubPostSummaryResponses
) {
    public static ClubPostSummaryListResponse from(List<ClubPostSummaryResponse> clubPostSummaryList) {
        return new ClubPostSummaryListResponse(clubPostSummaryList);
    }
}
