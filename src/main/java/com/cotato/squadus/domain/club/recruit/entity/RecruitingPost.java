package com.cotato.squadus.domain.club.recruit.entity;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "recruiting_post")
@NoArgsConstructor
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


    @ElementCollection
    @CollectionTable(name = "recruiting_post_questions", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "question")
    private List<String> questions = new ArrayList<>();

    @Builder
    public RecruitingPost(String title, Boolean isActive, LocalDate startDate, LocalDate endDate, ClubAdminMember author, Club club, List<String> questions) {
        this.title = title;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.author = author;
        this.club = club;
        this.questions = questions;
    }

//    private Long views;
//
//    private Long likes;

}
