package com.cotato.squadus.domain.club.match.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "match_post")
public class MatchPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_club_id")
    private Club homeClub;

    @OneToMany(mappedBy = "matchPost", cascade = CascadeType.ALL)
    private List<MatchRequest> matchRequests = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SportsCategory sportsCategory;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Embedded
    private MatchPlace matchPlace;

    private Boolean placeProvided;

    //월, 일 정보 저장
    private LocalDate matchStartDate;

    //시간 정보 저장
    private LocalTime matchStartTime;

    private Integer currentParticipants; // 현재 참가 인원

    private Integer maxParticipants; // 최대 참가 인원

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "matcher_id")
//    private ClubMember matcher;


    @Builder
    public MatchPost(Club homeClub, SportsCategory sportsCategory, String title, String content,
                     Tier tier, MatchPlace matchPlace, Boolean placeProvided, LocalDate matchStartDate, LocalTime matchStartTime,
                     Integer currentParticipants, Integer maxParticipants) {
        this.homeClub = homeClub;
//        this.awayClub = awayClub;
        this.sportsCategory = sportsCategory;
        this.title = title;
        this.content = content;
        this.tier = tier;
        this.matchPlace = matchPlace;
        this.placeProvided = placeProvided;
        this.matchStartDate = matchStartDate;
        this.matchStartTime = matchStartTime;
        this.currentParticipants = currentParticipants;
        this.maxParticipants = maxParticipants;
    }

    public void setHomeClub(Club homeClub) {
        this.homeClub = homeClub;
    }

    public void addMatchRequest(MatchRequest matchRequest) {
        this.matchRequests.add(matchRequest);
        matchRequest.setMatchPost(this);
    }

}
