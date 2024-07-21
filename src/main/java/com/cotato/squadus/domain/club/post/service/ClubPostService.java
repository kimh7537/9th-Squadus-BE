package com.cotato.squadus.domain.club.post.service;

import com.cotato.squadus.api.post.dto.ClubPostListResponse;
import com.cotato.squadus.api.post.dto.ClubPostResponse;
import com.cotato.squadus.api.post.dto.ClubPostSummaryListResponse;
import com.cotato.squadus.api.post.dto.ClubPostSummaryResponse;
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

    // 공지 전체 내용 조회
    public ClubPostListResponse findAllClubPostsByClubId(Long clubId) {
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

    public ClubPostResponse findClubPostByPostId(Long postId) {
        ClubPost clubPost = clubPostRepository.findByPostId(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리 공지를 찾을 수 없습니다."));
        ClubPostResponse clubPostResponse = ClubPostResponse.from(clubPost);
        return clubPostResponse;
    }

}
