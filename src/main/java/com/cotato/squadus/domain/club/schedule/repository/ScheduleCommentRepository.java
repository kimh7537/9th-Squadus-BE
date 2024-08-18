package com.cotato.squadus.domain.club.schedule.repository;


import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleCommentRepository extends JpaRepository<ScheduleComment, Long> {
    List<ScheduleComment> findByClubScheduleScheduleIdx(Long scheduleIdx);
}
