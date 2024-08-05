package com.cotato.squadus.api.schedule.dto;

import java.util.List;

public record ScheduleCommentListResponse(List<ScheduleCommentResponse> scheduleCommentResponses) {
    public static ScheduleCommentListResponse from(List<ScheduleCommentResponse> scheduleCommentResponses) {
        return new ScheduleCommentListResponse(scheduleCommentResponses);
    }
}