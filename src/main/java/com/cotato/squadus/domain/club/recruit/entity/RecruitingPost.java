package com.cotato.squadus.domain.club.recruit.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "recruiting_post")
public class RecruitingPost extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long postId;

    private String title;

    private Boolean isActive;

    private LocalDate startDate;
    private LocalDate endDate;

//    private String content;

//    private String image;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubAdminMember author;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

//    private Long views;
//
//    private Long likes;

}
