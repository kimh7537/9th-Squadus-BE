package com.cotato.squadus.domain.club.schedule.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import com.cotato.squadus.domain.club.schedule.enums.ScheduleCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "club_schedule")
@EntityListeners(AuditingEntityListener.class)
public class ClubSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private String title;

    @Enumerated(EnumType.STRING)
    private ScheduleCategory scheduleCategory;

    private String content;

    @OneToMany(mappedBy = "clubSchedule", cascade = ALL)
    private List<ScheduleComment> scheduleComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_idx")
    private ClubAdminMember author;

    private String location; // 추후에 Address 클래스로 바뀔 여지 있음

    private String equipment;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

//    private List<Vote> votes;
//    private List<Participant> participants;

    @Builder
    public ClubSchedule(Club club, String title, ScheduleCategory scheduleCategory, String content, ClubAdminMember author, String location, String equipment, LocalDate date) {
        this.club = club;
        this.title = title;
        this.scheduleCategory = scheduleCategory;
        this.content = content;
        this.author = author;
        this.location = location;
        this.equipment = equipment;
        this.date = date;
    }

    public void update(String title, ScheduleCategory scheduleCategory, String content, String location, String equipment, LocalDate date) {
        this.title = title;
        this.scheduleCategory = scheduleCategory;
        this.content = content;
        this.location = location;
        this.equipment = equipment;
        this.date = date;
    }
}
