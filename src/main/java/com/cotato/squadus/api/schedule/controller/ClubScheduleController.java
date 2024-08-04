package com.cotato.squadus.api.schedule.controller;

import com.cotato.squadus.api.schedule.dto.ClubScheduleListResponse;
import com.cotato.squadus.api.schedule.dto.ClubScheduleRequest;
import com.cotato.squadus.api.schedule.dto.ClubScheduleResponse;
import com.cotato.squadus.domain.club.schedule.service.ClubScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/clubs/{clubId}/schedules")
@RequiredArgsConstructor
public class ClubScheduleController {

    private final ClubScheduleService clubScheduleService;

    @GetMapping
    public ResponseEntity<ClubScheduleListResponse> getAllSchedules(@PathVariable Long clubId) {
        List<ClubScheduleResponse> schedules = clubScheduleService.findAllSchedules(clubId);
        return ResponseEntity.ok(ClubScheduleListResponse.from(schedules));
    }

    @GetMapping("/date")
    public ResponseEntity<ClubScheduleListResponse> getSchedulesByDate(@PathVariable Long clubId,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ClubScheduleResponse> schedules = clubScheduleService.findSchedulesByDate(clubId, date);
        return ResponseEntity.ok(ClubScheduleListResponse.from(schedules));
    }

    @GetMapping("/month")
    public ResponseEntity<ClubScheduleListResponse> getSchedulesByMonth(@PathVariable Long clubId,
                                                                        @RequestParam int year,
                                                                        @RequestParam int month) {
        List<ClubScheduleResponse> schedules = clubScheduleService.findSchedulesByYearMonth(clubId, year, month);
        return ResponseEntity.ok(ClubScheduleListResponse.from(schedules));
    }

    @PostMapping
    public ResponseEntity<ClubScheduleResponse> createSchedule(@PathVariable Long clubId, @RequestBody ClubScheduleRequest scheduleRequest) {
        ClubScheduleResponse schedule = clubScheduleService.createSchedule(clubId, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId, @RequestParam Long adminId) {
        clubScheduleService.deleteSchedule(clubId, scheduleId, adminId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ClubScheduleResponse> updateSchedule(@PathVariable Long clubId, @PathVariable Long scheduleId, @RequestBody ClubScheduleRequest scheduleRequest) {
        ClubScheduleResponse schedule = clubScheduleService.updateSchedule(clubId, scheduleId, scheduleRequest);
        return ResponseEntity.ok(schedule);
    }
}