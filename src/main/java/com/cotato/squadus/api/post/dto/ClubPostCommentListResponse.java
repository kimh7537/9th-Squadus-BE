package com.cotato.squadus.api.post.dto;

import java.util.List;

public record ClubPostCommentListResponse(
        List<ClubPostCommentResponse> clubPostCommentResponseList
) {

    public static ClubPostCommentListResponse from(List<ClubPostCommentResponse> clubPostCommentResponseList) {
        return new ClubPostCommentListResponse(clubPostCommentResponseList);
    }
}
