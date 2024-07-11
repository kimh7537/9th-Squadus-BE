package com.cotato.squadus.domain.club.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club")
public class Club {

    @Id @GeneratedValue
    private Long clubIdx;

    private String clubName;

    private String university;

    private String clubTier;

    private Integer clubRank;

    private String sportsCategory; // 이 부분 바꿔야할 듯

    private String logo;

    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = ALL)
    private List<ClubMember> clubMembers;

    @Builder
    private Club(String clubName, String university, String sportsCategory, String logo) {
        this.clubName = clubName;
        this.university = university;
        this.sportsCategory = sportsCategory;
        this.logo = logo;
    }
}
