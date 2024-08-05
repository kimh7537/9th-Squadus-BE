package com.cotato.squadus.domain.club.post.service;

import com.cotato.squadus.api.post.dto.*;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.post.entity.ClubPost;
import com.cotato.squadus.domain.club.post.repository.ClubPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubPostService {

    private final ClubPostRepository clubPostRepository;
    private final ClubMemberService clubMemberService;

    // 공지 전체 내용 조회
    public ClubPostListResponse findAllClubPostsByClubId(Long clubId) {
        ClubMember clubMember = clubMemberService.findClubMemberBySecurityContextHolder();
        log.info("clubId: {} ", clubMember.getClub().getClubId());
        if(!clubMember.getClub().getClubId().equals(clubId)) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }
        List<ClubPostResponse> clubPostResponses = clubPostRepository.findAllByClub_ClubId(clubId)
                .stream()
                .map(ClubPostResponse::from)
                .toList();

        return ClubPostListResponse.from(clubPostResponses);
    }

    // id, 제목, 날짜
    public ClubPostSummaryListResponse findAllClubPostsSummaryByClubId(Long clubId) {
        List<ClubPostSummaryResponse> clubPostSummaryResponseList = clubPostRepository.findAllByClub_ClubId(clubId)
                .stream()
                .map(ClubPostSummaryResponse::from)
                .toList();

        return ClubPostSummaryListResponse.from(clubPostSummaryResponseList);
    }

    @Transactional
    public ClubPostResponse findClubPostByPostId(Long postId) {
        ClubPost clubPost = clubPostRepository.findByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리 공지를 찾을 수 없습니다."));
        clubPost.increaseViews();
        clubPostRepository.save(clubPost);
        log.info("조회수 증가, 조회수 : {}", clubPost.getViews());
        ClubPostResponse clubPostResponse = ClubPostResponse.from(clubPost);
        return clubPostResponse;
    }

    @Transactional
    public ClubPostCreateResponse createClubPost(ClubPostCreateRequest clubPostCreateRequest) {
        ClubPost clubPost = ClubPost.builder()
                .title(clubPostCreateRequest.title())
                .content(clubPostCreateRequest.content())
                .image(clubPostCreateRequest.imageUrl())
                .views(0L)
                .likes(0L)
                .build();
        ClubPost savedClubPost = clubPostRepository.save(clubPost);
        return new ClubPostCreateResponse(savedClubPost.getPostId());
    }

    @Transactional
    public ClubPostLikesResponse increaseClubPostLikes(Long postId) {
        ClubPost clubPost = clubPostRepository.findByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리 공지를 찾을 수 없습니다."));
        ClubPost likesIncreasedPost = clubPost.increaseLikes();
        ClubPost updated = clubPostRepository.save(likesIncreasedPost);
        return new ClubPostLikesResponse(updated.getLikes());
    }
}
