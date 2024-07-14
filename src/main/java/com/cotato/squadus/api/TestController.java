package com.cotato.squadus.api;

import com.cotato.squadus.api.admin.dto.ArticleDto;
import com.cotato.squadus.domain.club.article.service.ArticleService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final ArticleService articleService;

    @GetMapping("/test")
    public String index() {
        log.debug("log 확인");
        return "Hello World";
    }

    @PostMapping
    public void group(@RequestBody @Valid ArticleDto articleDto){
        articleService.createArticle(articleDto);
    }

    @GetMapping(value = "/{articleIdx}", produces = "application/json")
    public ArticleDto get(@PathVariable Long articleIdx){
        return articleService.getArticle(articleIdx);
    }

}

