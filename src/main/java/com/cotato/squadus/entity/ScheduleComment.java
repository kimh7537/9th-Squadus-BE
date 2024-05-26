package com.cotato.squadus.entity;

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
