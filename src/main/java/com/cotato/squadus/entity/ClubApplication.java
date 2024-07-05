package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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

    private LocalDateTime appliedAt;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Builder
    public ClubApplication(Member member, Club club, LocalDateTime appliedAt, ApplicationStatus applicationStatus) {
        this.member = member;
        this.club = club;
        this.appliedAt = appliedAt;
        this.applicationStatus = applicationStatus;
    }
}
