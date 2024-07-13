package com.cotato.squadus.domain.club.common.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "recruiting_post")
public class RecruitingPost {

    @Id @GeneratedValue
    private Long postIdx;

    private String title;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubAdminMember author;

    private LocalDateTime createdAt;

    private Long views;

    private Long likes;

}
