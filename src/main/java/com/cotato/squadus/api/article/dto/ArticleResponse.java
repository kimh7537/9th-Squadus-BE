package com.cotato.squadus.api.article.dto;

import java.time.LocalDateTime;

import com.cotato.squadus.domain.club.article.entity.Article;

public record ArticleResponse(
	Long articleId,
	String title,
	String subtitle,
	String type,
	String tag,
	String content,
	Long views,
	String imageUrl,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static ArticleResponse from(Article article) {
		return new ArticleResponse(
			article.getArticleIdx(),
			article.getTitle(),
			article.getSubtitle(),
			article.getType(),
			article.getTag(),
			article.getContent(),
			article.getViews(),
			article.getImageUrl(),
			article.getCreatedAt(),
			article.getModifiedAt()
		);
	}
}
