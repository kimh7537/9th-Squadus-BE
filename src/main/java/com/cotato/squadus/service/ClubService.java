package com.cotato.squadus.service;

import com.cotato.squadus.dto.ClubCreateRequest;
import com.cotato.squadus.dto.ClubCreateResponse;
import com.cotato.squadus.entity.Club;
import com.cotato.squadus.repository.ClubRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public ClubCreateResponse createClub(ClubCreateRequest clubCreateRequest) {
        Club club = Club.builder()
                .clubName(clubCreateRequest.getClubName())
                .university(clubCreateRequest.getUniversity())
                .sportsCategory(clubCreateRequest.getSportsCategory())
                .logo(clubCreateRequest.getLogo())
                .build();

        clubRepository.save(club);
        return new ClubCreateResponse(club.getClubIdx());
    }

}
