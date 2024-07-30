package com.cotato.squadus.api.article.dto;

import com.cotato.squadus.domain.club.article.entity.Article;
import lombok.Getter;

public record ArticleSummaryResponse(Long articleId, String title, String subtitle) {

    public static ArticleSummaryResponse from(Article article) {
        return new ArticleSummaryResponse(
                article.getArticleIdx(),
                article.getTitle(),
                article.getSubtitle()
        );
    }
}
