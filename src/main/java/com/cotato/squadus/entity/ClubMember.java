package com.cotato.squadus.entity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "club_member")
public class ClubMember {

    @Id @GeneratedValue
    private Long clubMemberIdx;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    @Enumerated(EnumType.STRING)
    private ClubRole role;

    @Enumerated(EnumType.STRING)
    private MemberState memberState;

    private Boolean isPaid;

    private String clubProfileImage;
}
