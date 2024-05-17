package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "club_schedule")
public class ClubSchedule {

    @Id @GeneratedValue
    private Long scheduleIdx;

    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    private String title;

    private String content;

    private Date date;

    private Date time;
}
