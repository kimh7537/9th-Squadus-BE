package com.cotato.squadus.api.article.dto;

import java.util.List;

public record ArticleListResponse(List<ArticleSummaryResponse> articles) {
    public static ArticleListResponse from(List<ArticleSummaryResponse> articles) {
        return new ArticleListResponse(articles);
    }
}