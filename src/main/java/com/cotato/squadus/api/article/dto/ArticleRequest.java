package com.cotato.squadus.api.article.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    private String subtitle;
    private String type;
    private String tag;
    private String content;
    private Long views;
}
