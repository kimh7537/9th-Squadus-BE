package com.cotato.squadus.api.schedule.controller;

import com.cotato.squadus.api.schedule.dto.ClubScheduleListResponse;
import com.cotato.squadus.api.schedule.dto.ClubScheduleRequest;
import com.cotato.squadus.api.schedule.dto.ClubScheduleResponse;
import com.cotato.squadus.domain.club.schedule.service.ClubScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "동아리 일정", description = "동아리 일정 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/schedules")
@RequiredArgsConstructor
public class ClubScheduleController {

    private final ClubScheduleService clubScheduleService;

    @GetMapping
    @Operation(summary = "동아리 일정 전체 조회", description = "clubId를 바탕으로 동아리 공지 댓글 전체를 조회합니다")
    public ResponseEntity<ClubScheduleListResponse> getAllSchedules(@PathVariable Long clubId) {
        List<ClubScheduleResponse> schedules = clubScheduleService.findAllSchedules(clubId);
        return ResponseEntity.ok(ClubScheduleListResponse.from(schedules));
    }

    @GetMapping("/date")
    @Operation(summary = "동아리 일정 날짜로 조회", description = "clubId와 설정한 날짜를 바탕으로 동아리 일정을 조회합니다")
    public ResponseEntity<ClubScheduleListResponse> getSchedulesByDate(@PathVariable Long clubId,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ClubScheduleResponse> schedules = clubScheduleService.findSchedulesByDate(clubId, date);
        return ResponseEntity.ok(ClubScheduleListResponse.from(schedules));
    }

    @PostMapping
    @Operation(summary = "동아리 일정 단건 생성", description = "clubId와 일정에 대한 정보를 바탕으로 동아리 일정을 생성합니다")
    public ResponseEntity<ClubScheduleResponse> createSchedule(@PathVariable Long clubId, @RequestBody ClubScheduleRequest scheduleRequest) {
        ClubScheduleResponse schedule = clubScheduleService.createSchedule(clubId, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "동아리 일정 단건 삭제", description = "clubId와 scheduleId를 바탕으로 동아리 일정을 삭제합니다")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId) {
        clubScheduleService.deleteSchedule(clubId, scheduleId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{scheduleId}")
    @Operation(summary = "동아리 일정 수정", description = "clubId와 scheduleId와 일정에 대한 바뀐 정보를 바탕으로 동아리 일정을 수정합니다")
    public ResponseEntity<ClubScheduleResponse> updateSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId, @RequestBody ClubScheduleRequest scheduleRequest) {
        ClubScheduleResponse schedule = clubScheduleService.updateSchedule(clubId, scheduleId, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }
}