package com.cotato.squadus.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "club_notice")
public class ClubNotice {

    @Id @GeneratedValue
    private Long postIdx;

    @ManyToOne
    @JoinColumn(name = "club_member_idx")
    private ClubMember author;

    private String title;

    private String content;

    private String image;

    private Date createdDate;

    private Integer views;

    private Integer likes;
}
