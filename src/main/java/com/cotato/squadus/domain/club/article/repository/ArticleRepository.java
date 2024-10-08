package com.cotato.squadus.domain.club.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
