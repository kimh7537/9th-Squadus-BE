package com.cotato.squadus.domain.club.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;

public interface ScheduleCommentRepository extends JpaRepository<ScheduleComment, Long> {
	List<ScheduleComment> findByClubScheduleScheduleIdx(Long scheduleIdx);
}
