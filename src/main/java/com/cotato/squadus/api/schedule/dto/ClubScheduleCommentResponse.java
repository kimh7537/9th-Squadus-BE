package com.cotato.squadus.api.schedule.dto;

import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;

import java.time.LocalDateTime;

public record ClubScheduleCommentResponse(
        Long commentId,
        String content,
        Long likes,
        LocalDateTime createdAt
) {
    public static ClubScheduleCommentResponse from(ScheduleComment comment) {
        return new ClubScheduleCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                comment.getCreatedAt()
        );
    }
}