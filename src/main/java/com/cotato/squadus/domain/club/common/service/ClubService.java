package com.cotato.squadus.domain.club.common.service;

import com.cotato.squadus.api.club.dto.*;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.s3.S3ImageService;
import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.auth.enums.Membership;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.auth.service.ClubMemberService;
import com.cotato.squadus.domain.auth.service.MemberService;
import com.cotato.squadus.domain.club.admin.service.ClubAdminService;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import com.cotato.squadus.domain.club.common.entity.Region;
import com.cotato.squadus.domain.club.common.enums.ClubCategory;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.repository.ClubApplicationRepository;
import com.cotato.squadus.domain.club.common.repository.ClubRepository;
import com.cotato.squadus.domain.auth.enums.ApplicationStatus;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import com.cotato.squadus.domain.auth.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubMemberService clubMemberService;
    private final ClubAdminService clubAdminService;
    private final MemberService memberService;
    private final S3ImageService s3ImageService;

    @Transactional
    public ClubCreateResponse createClub(CustomOAuth2Member customOAuth2Member, ClubCreateRequest clubCreateRequest, MultipartFile logoImage) {

        Member member = memberService.findMemberByUniqueId(customOAuth2Member.getUniqueId());

        // logo 설정
        String logo = null;
        if (logoImage != null && !logoImage.isEmpty()) {
            logo = s3ImageService.upload(logoImage);
        } else {
            logo = "default_logo.jpg"; // 기본 로고 이미지 URL
        }

        // ClubCategory에 따른 university 값 설정
        String university;
        if (clubCreateRequest.getClubCategory() == ClubCategory.UNION) {
            university = "no university";
        } else {
            university = member.getUniversity();
        }

        Club club = Club.builder()
                .clubName(clubCreateRequest.getClubName())
                .university(university)
                .clubCategory(clubCreateRequest.getClubCategory())
                .sportsCategory(clubCreateRequest.getSportsCategory())
                .logo(logo)
                .clubTier(ClubTier.BRONZE)
                .clubMessage(clubCreateRequest.getClubMessage())
                .maxMembers(clubCreateRequest.getMaxMembers())
                .tags(clubCreateRequest.getTags())
                .region(Region.builder()
                        .city(clubCreateRequest.getCity())
                        .district(clubCreateRequest.getDistrict())
                        .build())
                .build();

        ClubAdminMember clubAdminMember = ClubAdminMember.builder()
                .member(member)
                .club(club)
                .membership(Membership.JOINED)
                .clubProfileImage("default.jpg")
                .adminStatus(AdminStatus.CURRENT)
                .isPaid(false)
                .build();

        clubMemberService.saveClubMember(clubAdminMember);

        club.addClubMember(clubAdminMember);

        Club savedClub = clubRepository.save(club);
        log.info("동아리 생성됨, clubId : {}", savedClub.getClubId());
        return new ClubCreateResponse(savedClub.getClubId());
    }

    @Transactional
    public ClubApplyResponse joinClub(CustomOAuth2Member customOAuth2Member, Long clubId, ClubApplyRequest clubApplyRequest) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리를 찾을 수 없습니다."));
        Member member = memberService.findMemberByUniqueId(customOAuth2Member.getUniqueId());
        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .appliedAt(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .answers(clubApplyRequest.getAnswers())
                .build();
        ClubApplication savedApplication = clubApplicationRepository.save(clubApplication);
        return new ClubApplyResponse(savedApplication.getApplicationIdx());
    }

    public Club findClubByClubId(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리를 찾을 수 없습니다."));

        return club;
    }

    public ClubInfoResponse findClubInfo(Long clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리를 찾을 수 없습니다."));

        ClubInfoResponse clubInfoResponse = ClubInfoResponse.from(club);
        return clubInfoResponse;
    }


    @Transactional
    public ClubUpdateResponse updateClub(CustomOAuth2Member customOAuth2Member, Long clubId, ClubUpdateRequest clubUpdateRequest, MultipartFile logoImage) {
        clubAdminService.validateAdminMember(clubId);

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 clubId를 가진 동아리가 존재하지 않습니다."));

        // logo 설정
        String logo = club.getLogo();
        if (logoImage != null && !logoImage.isEmpty()) {
            logo = s3ImageService.upload(logoImage);
        }

        Club updateClub = club.updateClub(
                logo,
                clubUpdateRequest.clubMessage(),
                Region.builder()
                        .city(clubUpdateRequest.city())
                        .district(clubUpdateRequest.district())
                        .build(),
                clubUpdateRequest.maxMembers(),
                clubUpdateRequest.tags()
        );

        Club savedClub = clubRepository.save(updateClub);

        return new ClubUpdateResponse(savedClub.getClubId());
    }

}
