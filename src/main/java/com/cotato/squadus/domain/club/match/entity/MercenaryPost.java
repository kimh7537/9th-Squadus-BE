package com.cotato.squadus.domain.club.match.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
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
@Table(name = "mercenary_post")
public class MercenaryPost  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mercenaryIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_club_id")
    private Club homeClub;

    @OneToMany(mappedBy = "mercenaryPost", cascade = CascadeType.ALL)
    private List<MercenaryRequest> mercenaryRequests = new ArrayList<>();

    private String title;

    @Lob
    private String content;

    @Embedded
    private MatchPlace matchPlace;

    private Boolean placeProvided;

    //월, 일 정보 저장
    private LocalDate matchStartDate;

    //시간 정보 저장
    private LocalTime matchStartTime;

    private Integer maxParticipants; // 최대 참가 인원

    private Integer currentParticipants = 0;


    @Builder
    public MercenaryPost(Club homeClub, String title, String content,
                     MatchPlace matchPlace, Boolean placeProvided, LocalDate matchStartDate, LocalTime matchStartTime,
                     Integer maxParticipants) {
        this.homeClub = homeClub;
        this.title = title;
        this.content = content;
        this.matchPlace = matchPlace;
        this.placeProvided = placeProvided;
        this.matchStartDate = matchStartDate;
        this.matchStartTime = matchStartTime;
        this.maxParticipants = maxParticipants;
    }

    public void setHomeClub(Club homeClub) {
        this.homeClub = homeClub;
    }

    public void addMercenaryRequest(MercenaryRequest mercenaryRequest) {
        this.mercenaryRequests.add(mercenaryRequest);
        mercenaryRequest.setMercenaryPost(this);
    }

    public void incrementParticipants() {
        if (this.currentParticipants < this.maxParticipants) {
            this.currentParticipants++;
        } else {
            throw new IllegalStateException("더 이상 참가할 수 없습니다. 최대 인원을 초과했습니다.");
        }
    }
}
