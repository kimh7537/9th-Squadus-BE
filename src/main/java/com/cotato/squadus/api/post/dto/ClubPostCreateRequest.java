package com.cotato.squadus.api.post.dto;

public record ClubPostCreateRequest(
        String title,
        String content,
        String imageUrl,
        Boolean hasVote,
        Boolean isImportant
) {
}
