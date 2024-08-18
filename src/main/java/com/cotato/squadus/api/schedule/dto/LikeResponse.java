package com.cotato.squadus.api.schedule.dto;

public record LikeResponse(Long commentId, Long likes) {
    public static LikeResponse from(Long commentId, Long likes) {
        return new LikeResponse(commentId, likes);
    }
}
