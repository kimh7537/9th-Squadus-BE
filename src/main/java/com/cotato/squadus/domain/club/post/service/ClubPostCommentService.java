package com.cotato.squadus.domain.club.post.service;

import com.cotato.squadus.api.post.dto.ClubPostCommentResponse;
import com.cotato.squadus.domain.club.post.repository.ClubPostCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubPostCommentService {

    private final ClubPostCommentRepository clubPostCommentRepository;

    public List<ClubPostCommentResponse> findAllClubPostComments(Long postId) {
        List<ClubPostCommentResponse> clubPostCommentResponseList = clubPostCommentRepository.findAllByClubPost_PostId(postId).stream()
                .map(ClubPostCommentResponse::from).toList();
        return clubPostCommentResponseList;

    }
}
