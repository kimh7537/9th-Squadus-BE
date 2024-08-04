package com.cotato.squadus.api.article.controller;

import com.cotato.squadus.api.article.dto.ArticleListResponse;
import com.cotato.squadus.api.article.dto.ArticleRequest;
import com.cotato.squadus.api.article.dto.ArticleResponse;
import com.cotato.squadus.api.article.dto.ArticleSummaryResponse;
import com.cotato.squadus.domain.club.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "아티클", description = "아티클 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{articleId}")
    @Operation(summary = "아티클 단건 조회", description = "articleId를 바탕으로 아티클 하나에 대한 정보를 조회합니다")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long articleId) {
        ArticleResponse article = articleService.findArticleById(articleId);
        log.info("ArticleId로 기사 조회 : {} ", article);
        return ResponseEntity.ok(article);
    }

    @PostMapping
    @Operation(summary = "아티클 단건 생성", description = "article에 대한 정보를 바탕으로 아티클 하나를 생성합니다")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest articleRequest) {
        ArticleResponse article = articleService.createArticle(articleRequest);
        log.info("새 기사 생성 : {} ", article);
        return ResponseEntity.ok(article);
    }

    @GetMapping
    @Operation(summary = "아티클 요약 전체 조회(페이징 단위: 10)", description = "article에 대한 정보를 10개 단위로 페이징 하여 조회합니다.")
    public ResponseEntity<Page<ArticleSummaryResponse>> getAllArticleSummaries(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ArticleSummaryResponse> articles = articleService.findAllArticleSummaries(pageable);
        log.info("모든 기사 요약 조회, 페이지 정보 : {} ", pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/all")
    @Operation(summary = "아티클 요약 전체 조회(페이징 없음)", description = "모든 아티클을 조회합니다.")
    public ResponseEntity<ArticleListResponse> getAllArticles() {
        List<ArticleSummaryResponse> articles = articleService.getAllArticles();
        log.info("모든 기사 요약 조회");
        return ResponseEntity.ok(ArticleListResponse.from(articles));
    }
}
