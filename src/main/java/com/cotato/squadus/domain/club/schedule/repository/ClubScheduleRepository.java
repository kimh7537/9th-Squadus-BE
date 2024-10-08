package com.cotato.squadus.domain.club.schedule.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;

public interface ClubScheduleRepository extends JpaRepository<ClubSchedule, Long> {

	List<ClubSchedule> findByClubClubId(Long clubId);

	List<ClubSchedule> findByClubClubIdAndDate(Long clubId, LocalDate date);

	List<ClubSchedule> findByClubClubIdAndDateBetween(Long clubId, LocalDate startDate, LocalDate endDate);

	Optional<ClubSchedule> findByScheduleIdxAndClubClubId(Long scheduleId, Long clubId);

	List<ClubSchedule> findByClubClubIdOrderByDateAscStartTimeAsc(Long clubId, Pageable pageable);
}