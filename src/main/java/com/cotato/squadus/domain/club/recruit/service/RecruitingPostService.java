package com.cotato.squadus.domain.club.recruit.service;

import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateRequest;
import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.service.ClubService;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;
import com.cotato.squadus.domain.club.recruit.repository.RecruitingPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitingPostService {

    private final RecruitingPostRepository recruitingPostRepository;
    private final ClubService clubService;


    public Page<RecruitingPostResponse> findAllRecruitingPosts(CustomOAuth2Member customOAuth2Member, Pageable pageable) {
        return recruitingPostRepository.findAll(pageable)
                .map(RecruitingPostResponse::from);
    }

    @Transactional
    public RecruitingPostCreateResponse createRecruitingPost(CustomOAuth2Member customOAuth2Member, RecruitingPostCreateRequest recruitingPostCreateRequest) {

        Club club = clubService.findClubByClubId(recruitingPostCreateRequest.clubId());
        RecruitingPost recruitingPost = RecruitingPost.builder()
                .title(recruitingPostCreateRequest.title())
                .club(club)
                .startDate(recruitingPostCreateRequest.startDate())
                .endDate(recruitingPostCreateRequest.endDate())
                .questions(recruitingPostCreateRequest.questions())
                .build();

        RecruitingPost saved = recruitingPostRepository.save(recruitingPost);
        return new RecruitingPostCreateResponse(saved.getPostId());
    }
}
