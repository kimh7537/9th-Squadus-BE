package com.cotato.squadus.domain.club.banner;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "banner")
public class Banner {

    @Id @GeneratedValue
    private Long bannerIdx;

    private String title;

    private String type;

    private LocalDateTime createdAt;

    private String content;

    private Long views;

}
