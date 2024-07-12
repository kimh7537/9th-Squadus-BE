package com.cotato.squadus.domain.club.schedule.entity;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "schedule_comment")
public class ScheduleComment {

    @Id @GeneratedValue
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private Long likes;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubMember clubMember;

    @ManyToOne
    @JoinColumn(name = "club_schedule")
    private ClubSchedule clubSchedule;
}
