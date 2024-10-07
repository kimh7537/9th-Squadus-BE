package com.cotato.squadus.api.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ClubScheduleResponse(
	Long scheduleIdx,
	String title,
	String scheduleCategory,
	String content,
	Long authorId,
	String location,
	String equipment,
	LocalDate date,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	LocalTime startTime,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	LocalTime endTime
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
			schedule.getDate(),
			schedule.getStartTime(),
			schedule.getEndTime()
		);
	}
}
