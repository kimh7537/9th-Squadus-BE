package com.cotato.squadus.domain.club.article.service;

import com.cotato.squadus.api.article.dto.ArticleRequest;
import com.cotato.squadus.api.article.dto.ArticleResponse;
import com.cotato.squadus.api.article.dto.ArticleSummaryResponse;
import com.cotato.squadus.common.s3.S3ImageService;
import com.cotato.squadus.domain.club.article.entity.Article;
import com.cotato.squadus.domain.club.article.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final S3ImageService s3ImageService;

    public ArticleResponse findArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 기사를 찾을 수 없습니다: " + articleId));
        article.setViews(article.getViews() + 1); // 조회수 증가
        articleRepository.save(article); // 변경된 조회수를 저장
        return ArticleResponse.from(article);
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest articleRequest, MultipartFile multipartFile) {

        String imageUrl = s3ImageService.upload(multipartFile);

        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .subtitle(articleRequest.getSubtitle())
                .type(articleRequest.getType())
                .tag(articleRequest.getTag())
                .content(articleRequest.getContent())
                .views(articleRequest.getViews())
                .imageUrl(imageUrl)
                .build();
        articleRepository.save(article);
        return ArticleResponse.from(article);
    }

    public Page<ArticleSummaryResponse> findAllArticleSummaries(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleSummaryResponse::from);
    }

    public List<ArticleSummaryResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> new ArticleSummaryResponse(article.getArticleIdx(), article.getTitle(), article.getSubtitle()))
                .collect(Collectors.toList());
    }
}
