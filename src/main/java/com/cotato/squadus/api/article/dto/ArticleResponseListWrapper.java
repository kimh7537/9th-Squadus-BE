package com.cotato.squadus.api.article.dto;

import java.util.List;

public record ArticleResponseListWrapper(List<ArticleResponse> articles) {

    public static ArticleResponseListWrapper from(List<ArticleResponse> articles) {
        return new ArticleResponseListWrapper(articles);
    }
}