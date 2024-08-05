package com.cotato.squadus.domain.club.schedule.service;

import com.cotato.squadus.api.schedule.dto.ClubScheduleRequest;
import com.cotato.squadus.api.schedule.dto.ClubScheduleResponse;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import com.cotato.squadus.domain.club.common.repository.ClubAdminMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;
import com.cotato.squadus.domain.club.schedule.enums.ScheduleCategory;
import com.cotato.squadus.domain.club.schedule.repository.ClubScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubScheduleService {

    private final ClubScheduleRepository clubScheduleRepository;
    private final ClubRepository clubRepository;
    private final ClubAdminMemberRepository clubAdminMemberRepository;

    //club의 모든 schedule을 가져옴
    public List<ClubScheduleResponse> findAllSchedules(Long clubId) {
        List<ClubSchedule> schedules = clubScheduleRepository.findByClubClubId(clubId);
        return schedules.stream()
                .map(ClubScheduleResponse::from)
                .collect(Collectors.toList());
    }

    //club의 특정한 날짜의 schedule들을 가져옴
    public List<ClubScheduleResponse> findSchedulesByDate(Long clubId, LocalDate date) {
        List<ClubSchedule> schedules = clubScheduleRepository.findByClubClubIdAndDate(clubId, date);
        return schedules.stream()
                .map(ClubScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // club의 특정 연도와 월의 schedule들을 가져옴
    public List<ClubScheduleResponse> findSchedulesByYearMonth(Long clubId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<ClubSchedule> schedules = clubScheduleRepository.findByClubClubIdAndDateBetween(clubId, startDate, endDate);
        return schedules.stream()
                .map(ClubScheduleResponse::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public ClubScheduleResponse createSchedule(Long clubId, ClubScheduleRequest scheduleRequest) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        ClubAdminMember adminMember = clubAdminMemberRepository.findByClubMemberIdxAndAdminStatus(scheduleRequest.getAuthorId(), AdminStatus.CURRENT)
                .orElseThrow(() -> new EntityNotFoundException("Admin member not found with id: " + scheduleRequest.getAuthorId()));

        ClubSchedule schedule = ClubSchedule.builder()
                .club(club)
                .title(scheduleRequest.getTitle())
                .scheduleCategory(ScheduleCategory.valueOf(scheduleRequest.getScheduleCategory()))
                .content(scheduleRequest.getContent())
                .author(adminMember)
                .location(scheduleRequest.getLocation())
                .equipment(scheduleRequest.getEquipment())
                .date(scheduleRequest.getDate())
                .startTime(scheduleRequest.getStartTime())
                .endTime(scheduleRequest.getEndTime())
                .build();

        clubScheduleRepository.save(schedule);
        return ClubScheduleResponse.from(schedule);
    }

    @Transactional
    public void deleteSchedule(Long clubId, Long scheduleId, Long adminId) {
        ClubSchedule schedule = clubScheduleRepository.findByScheduleIdxAndClubClubId(scheduleId, clubId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));

        ClubAdminMember adminMember = clubAdminMemberRepository.findByClubMemberIdxAndAdminStatus(adminId, AdminStatus.CURRENT)
                .orElseThrow(() -> new EntityNotFoundException("Admin member not found or not CURRENT with id: " + adminId));

        if (!schedule.getAuthor().getClubMemberIdx().equals(adminMember.getClubMemberIdx())) {
            throw new IllegalArgumentException("You do not have permission to delete this schedule.");
        }

        clubScheduleRepository.delete(schedule);
    }


    @Transactional
    public ClubScheduleResponse updateSchedule(Long clubId, Long scheduleId, ClubScheduleRequest scheduleRequest) {
        ClubSchedule schedule = clubScheduleRepository.findByScheduleIdxAndClubClubId(scheduleId, clubId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));

        ClubAdminMember adminMember = clubAdminMemberRepository.findByClubMemberIdxAndAdminStatus(scheduleRequest.getAuthorId(), AdminStatus.CURRENT)
                .orElseThrow(() -> new EntityNotFoundException("Admin member not found or not CURRENT with id: " + scheduleRequest.getAuthorId()));

        if (!schedule.getAuthor().getClubMemberIdx().equals(adminMember.getClubMemberIdx())) {
            throw new IllegalArgumentException("You do not have permission to update this schedule.");
        }

        schedule.update(
                scheduleRequest.getTitle(),
                ScheduleCategory.valueOf(scheduleRequest.getScheduleCategory()),
                scheduleRequest.getContent(),
                scheduleRequest.getLocation(),
                scheduleRequest.getEquipment(),
                scheduleRequest.getDate(),
                scheduleRequest.getStartTime(),
                scheduleRequest.getEndTime()
        );

        clubScheduleRepository.save(schedule);
        return ClubScheduleResponse.from(schedule);
    }
}
