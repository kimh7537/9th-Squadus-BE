package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;
import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
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

    @OneToMany(fetch = FetchType.LAZY, cascade = ALL)
    private List<ClubMember> clubMembers;
}
