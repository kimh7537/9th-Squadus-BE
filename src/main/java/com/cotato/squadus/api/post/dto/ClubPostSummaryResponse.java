package com.cotato.squadus.api.post.dto;

import com.cotato.squadus.domain.club.post.entity.ClubPost;

import java.time.LocalDateTime;

public record ClubPostSummaryResponse(
        Long postId,
        String title,
        LocalDateTime createdAt
) {

    public static ClubPostSummaryResponse from(ClubPost clubPost) {
        return new ClubPostSummaryResponse(
                clubPost.getPostId(),
                clubPost.getTitle(),
                clubPost.getCreatedAt()
        );
    }
}