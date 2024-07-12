package com.cotato.squadus.domain.club.article.repository;

import com.cotato.squadus.domain.club.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
