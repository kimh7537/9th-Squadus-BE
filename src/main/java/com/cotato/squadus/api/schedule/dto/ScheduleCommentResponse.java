package com.cotato.squadus.api.schedule.dto;

import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;

import java.time.LocalDateTime;

public record ScheduleCommentResponse(
        Long commentId,
        Long memberId,
        String content,
        Long likes
) {
    public static ScheduleCommentResponse from(ScheduleComment comment) {
        return new ScheduleCommentResponse(
                comment.getId(),
                comment.getClubMember().getClubMemberIdx(),
                comment.getContent(),
                comment.getLikes()
        );
    }
}
