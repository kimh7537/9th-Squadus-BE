package com.cotato.squadus.domain.club.recruit.service;

import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateRequest;
import com.cotato.squadus.api.recruit.dto.RecruitingPostCreateResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostInfoResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.service.ClubService;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;
import com.cotato.squadus.domain.club.recruit.repository.RecruitingPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecruitingPostService {

    private final RecruitingPostRepository recruitingPostRepository;
    private final ClubService clubService;


    public Page<RecruitingPostResponse> findAllRecruitingPosts(CustomOAuth2Member customOAuth2Member, Pageable pageable) {
        return recruitingPostRepository.findAllWithClub(pageable)
                .map(RecruitingPostResponse::from);
    }

    public RecruitingPostInfoResponse findRecruitingPostByPostId(CustomOAuth2Member customOAuth2Member, Long postId) {
        RecruitingPost recruitingPost = recruitingPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 홍보 게시글을 찾을 수 없습니다."));

        return RecruitingPostInfoResponse.from(recruitingPost);
    }

    @Transactional
    public RecruitingPostCreateResponse createRecruitingPost(CustomOAuth2Member customOAuth2Member, @RequestBody RecruitingPostCreateRequest recruitingPostCreateRequest) {

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
