package com.cotato.squadus.domain.club.post.entity;

import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "club_post")
public class ClubPost {

    @Id @GeneratedValue
    private Long postIdx;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubAdminMember author;

    private String title;

    private String content;

    private String image;

    private LocalDateTime createdAt;

    private Long views;

    private Long likes;
}
