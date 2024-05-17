package com.cotato.squadus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "banner")
public class Banner {

    @Id @GeneratedValue
    private Long bannerIdx;

    private String title;

    private String type;

    private Date createdDate;

    private String content;

    private Integer views;

}
