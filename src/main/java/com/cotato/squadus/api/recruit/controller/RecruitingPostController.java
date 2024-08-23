package com.cotato.squadus.api.recruit.controller;


import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateRequest;
import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostInfoResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.club.recruit.service.RecruitingPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "동아리 홍보 글", description = "동아리 홍보글 관련 API")
@Slf4j
@RestController
@RequestMapping("/v1/api/recruitments")
@RequiredArgsConstructor
public class RecruitingPostController {

    private final RecruitingPostService recruitingPostService;

    @GetMapping()
    @Operation(summary = "동아리 홍보글 전체 조회(페이징 단위: 10)", description = "동아리 홍보글 전체를 10개씩 페이징하여 조회합니다.")
    public ResponseEntity<Page<RecruitingPostResponse>> findAllRecruitingPosts(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PageableDefault(size = 10) Pageable pageable) {
        Page<RecruitingPostResponse> allRecruitingPosts = recruitingPostService.findAllRecruitingPosts(customOAuth2Member, pageable);
        return ResponseEntity.ok(allRecruitingPosts);
    }

    @GetMapping("/{recruitingPostId}")
    @Operation(summary = "동아리 홍보글 단건 조회", description = "홍보글의 id를 통해 동아리 홍보글 하나에 대한 정보를 조회합니다.")
    public ResponseEntity<RecruitingPostInfoResponse> findRecruitingPostByPostId(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long recruitingPostId) {
        RecruitingPostInfoResponse recruitingPostByPostId = recruitingPostService.findRecruitingPostByPostId(customOAuth2Member, recruitingPostId);
        return ResponseEntity.ok(recruitingPostByPostId);
    }

    @PostMapping
    @Operation(summary = "동아리 홍보글 생성", description = "동아리 홍보글을 생성합니다")
    public ResponseEntity<RecruitingPostCreateResponse> createRecruitingPost(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody RecruitingPostCreateRequest recruitingPostCreateRequest) {
        RecruitingPostCreateResponse recruitingPost = recruitingPostService.createRecruitingPost(customOAuth2Member, recruitingPostCreateRequest);
        return ResponseEntity.ok(recruitingPost);
    }



}
