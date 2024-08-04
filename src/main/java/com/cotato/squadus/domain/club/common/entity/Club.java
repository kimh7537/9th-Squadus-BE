package com.cotato.squadus.domain.club.common.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.post.entity.ClubPost;
import com.cotato.squadus.domain.club.schedule.entity.ClubSchedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club")
@EntityListeners(AuditingEntityListener.class)
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    private String clubName;

    private String university;

    private ClubTier clubTier;

    private Integer clubRank;

    @Enumerated(EnumType.STRING)
    private SportsCategory sportsCategory;

    private String logo;

    @OneToMany(mappedBy = "club", cascade = ALL)
    private List<ClubSchedule> clubSchedules;

    @OneToMany(mappedBy = "club", cascade = ALL)
    private List<ClubMember> clubMembers;

    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = ALL)
    private List<ClubPost> clubPosts;

    @Builder
    private Club(String clubName, String university, SportsCategory sportsCategory, String logo, ClubTier clubTier, Integer clubRank) {
        this.clubName = clubName;
        this.university = university;
        this.sportsCategory = sportsCategory;
        this.logo = logo;
        this.clubTier = clubTier;
        this.clubRank = clubRank;
    }
}
