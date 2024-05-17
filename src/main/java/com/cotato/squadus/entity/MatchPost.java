package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "match_post")
public class MatchPost {

    @Id @GeneratedValue
    private Long matchIdx;

    @ManyToOne
    @JoinColumn(name = "home_club_idx")
    private Club homeClub;

    @ManyToOne
    @JoinColumn(name = "away_club_idx")
    private Club awayClub;

    private Date matchDate;

    private String matchResult;

    private String matchPlace;

    private String matchType;

    @Enumerated(EnumType.STRING)
    private Tier matchTier;

    @ManyToOne
    @JoinColumn(name = "matcher_idx")
    private ClubMember matcher;
}
