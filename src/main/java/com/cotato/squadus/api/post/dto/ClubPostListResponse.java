package com.cotato.squadus.api.post.dto;

import com.cotato.squadus.domain.club.post.entity.ClubPost;

import java.util.List;

public record ClubPostListResponse(
    List<ClubPostResponse> clubPostIds
) {


    public static ClubPostListResponse from(List<ClubPostResponse> clubPosts) {
        return new ClubPostListResponse(clubPosts);
    }
}
