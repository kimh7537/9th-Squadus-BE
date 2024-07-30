package com.cotato.squadus.domain.club.schedule.repository;

import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClubScheduleRepository extends JpaRepository<ClubSchedule, Long> {

    List<ClubSchedule> findByClubClubId(Long clubId);

    List<ClubSchedule> findByClubClubIdAndDate(Long clubId, LocalDate date);

    Optional<ClubSchedule> findByScheduleIdxAndClubClubId(Long scheduleId, Long clubId);
}