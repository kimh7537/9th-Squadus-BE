package com.cotato.squadus.domain.club.post.service;

import com.cotato.squadus.api.post.dto.ClubPostListResponse;
import com.cotato.squadus.api.post.dto.ClubPostResponse;
import com.cotato.squadus.domain.club.post.repository.ClubPostRepository;
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

    public ClubPostListResponse findAllClubPostsByClubId(Long clubIdx) {
        List<ClubPostResponse> clubPostResponses = clubPostRepository.findAllByClub_ClubIdx(clubIdx)
                        .stream()
                        .map(ClubPostResponse::from)
                        .toList();

        return ClubPostListResponse.from(clubPostResponses);
    }
}
