package com.cotato.squadus.domain.club.common.entity;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.enums.MemberStatus;
import com.cotato.squadus.domain.club.schedule.entity.ScheduleComment;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "member_type")
@Table(name = "club_member")
public abstract class ClubMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubMemberIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    private Boolean isPaid;

    //s3로 이미지 url 저장
    private String clubProfileImage;

    @OneToMany(mappedBy = "clubMember", cascade = ALL)
    private List<ScheduleComment> scheduleComments;
}
