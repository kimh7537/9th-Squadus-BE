package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "club_promotion_post")
public class ClubPromotionPost {

    @Id @GeneratedValue
    private Long postIdx;

    private String title;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubMember author;

    private Date createdDate;

    private Integer views;

    private Integer likes;

}
