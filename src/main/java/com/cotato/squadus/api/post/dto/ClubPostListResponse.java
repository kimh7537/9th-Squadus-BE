package com.cotato.squadus.api.post.dto;
import java.util.List;

public record ClubPostListResponse(
    List<ClubPostResponse> clubPosts
) {


    public static ClubPostListResponse from(List<ClubPostResponse> clubPosts) {
        return new ClubPostListResponse(clubPosts);
    }
}
