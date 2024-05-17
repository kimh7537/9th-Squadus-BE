package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "club_application")
public class ClubApplication {

    @Id @GeneratedValue
    private Long applicationIdx;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    private Date applicationDate;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
