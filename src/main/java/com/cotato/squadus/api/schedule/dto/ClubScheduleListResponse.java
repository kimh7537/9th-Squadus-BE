package com.cotato.squadus.api.schedule.dto;

import java.util.List;

public record ClubScheduleListResponse(List<ClubScheduleResponse> schedules) {
    public static ClubScheduleListResponse from(List<ClubScheduleResponse> schedules) {
        return new ClubScheduleListResponse(schedules);
    }
}
