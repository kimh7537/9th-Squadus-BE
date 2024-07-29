package com.cotato.squadus.api.schedule.dto;

import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;

import java.time.LocalDate;

public record ClubScheduleResponse(
        Long scheduleIdx,
        String title,
        String scheduleCategory,
        String content,
        Long authorId,
        String location,
        String equipment,
        LocalDate date
) {
    public static ClubScheduleResponse from(ClubSchedule schedule) {
        return new ClubScheduleResponse(
                schedule.getScheduleIdx(),
                schedule.getTitle(),
                schedule.getScheduleCategory().name(),
                schedule.getContent(),
                schedule.getAuthor().getClubMemberIdx(),
                schedule.getLocation(),
                schedule.getEquipment(),
                schedule.getDate()
        );
    }
}
