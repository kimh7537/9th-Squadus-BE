package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue
    private Long memberIdx;

    private String memberId;

    private String email;

    private String profileImage;

    private String university;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToMany(mappedBy = "member", fetch = LAZY, cascade = ALL)
    private List<ClubMember> clubMemberships;
}
