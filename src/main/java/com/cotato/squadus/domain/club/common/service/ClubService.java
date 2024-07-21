package com.cotato.squadus.domain.club.common.service;

import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.club.common.repository.ClubApplicationRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.api.club.dto.ClubCreateRequest;
import com.cotato.squadus.api.club.dto.ClubCreateResponse;
import com.cotato.squadus.api.club.dto.ClubApplyRequest;
import com.cotato.squadus.api.club.dto.ClubApplyResponse;
import com.cotato.squadus.domain.auth.enums.ApplicationStatus;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import com.cotato.squadus.domain.auth.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubApplicationRepository clubApplicationRepository;

    /**
     *
     * @param clubCreateRequest 동아리 생성 dto
     * @return ClubCreateResponse 생성된 동아리 id
     */
    @Transactional
    public ClubCreateResponse createClub(ClubCreateRequest clubCreateRequest) {
        Club club = Club.builder()
                .clubName(clubCreateRequest.getClubName())
                .university(clubCreateRequest.getUniversity())
                .sportsCategory(clubCreateRequest.getSportsCategory())
                .logo(clubCreateRequest.getLogo())
                .build();

        clubRepository.save(club);
        log.info("Club created");
        return new ClubCreateResponse(club.getClubId());
    }

    /**
     *
     * @param clubApplyRequest 동아리 가입 신청 dto
     * @return ClubApplyResponse clubApplication id 담아서 리턴
     * 동아리 가입 신청하는 매서드입니다.
     * 초기에는 상태 PENDING으로 설정되고 나중에 동아리장이 가입 승인하면 바뀜
     */
    @Transactional
    public ClubApplyResponse joinClub(ClubApplyRequest clubApplyRequest) {
        Club club = clubRepository.findById(clubApplyRequest.getClubIdx()).get();
        Member member = memberRepository.findById(clubApplyRequest.getMemberIdx()).get();
        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .appliedAt(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .build();
        ClubApplication savedApplication = clubApplicationRepository.save(clubApplication);
        return new ClubApplyResponse(savedApplication.getApplicationIdx());
    }

}
