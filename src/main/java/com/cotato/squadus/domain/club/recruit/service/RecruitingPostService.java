package com.cotato.squadus.domain.club.recruit.service;

import com.cotato.squadus.api.recruit.dto.RecruitingPostResponse;
import com.cotato.squadus.api.recruit.dto.RecruitingPostResponseList;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
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


    public Page<RecruitingPostResponse> findAllRecruitingPosts(CustomOAuth2Member customOAuth2Member, Pageable pageable) {
        return recruitingPostRepository.findAll(pageable)
                .map(RecruitingPostResponse::from);
    }
}
