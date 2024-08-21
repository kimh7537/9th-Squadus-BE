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
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ClubApplyResponse joinClub(Long clubId, ClubApplyRequest clubApplyRequest) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리를 찾을 수 없습니다."));
        Member member = memberRepository.findById(clubApplyRequest.getMemberIdx())
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 회원을 찾을 수 없습니다."));
        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .appliedAt(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
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


    public ClubTierInfoResponse getClubTierInfo(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        SportsCategory category = club.getSportsCategory();
        List<Club> clubs = clubRepository.findBySportsCategoryOrderByMatchScoreDesc(category);

        int totalClubs = clubs.size();
        int currentRank = 0;

        for (int i = 0; i < totalClubs; i++) {
            if (clubs.get(i).getClubId().equals(clubId)) {
                currentRank = i + 1; // 1-based index
                break;
            }
        }

        int ranksToNextTier = 0;
        if (club.getClubTier() == ClubTier.BRONZE) {
            int silverCutoff = (int) Math.ceil(totalClubs * 0.5);
            ranksToNextTier = Math.max(0, silverCutoff - currentRank);
        } else if (club.getClubTier() == ClubTier.SILVER) {
            int goldCutoff = (int) Math.ceil(totalClubs * 0.2);
            ranksToNextTier = Math.max(0, goldCutoff - currentRank);
        }

        return new ClubTierInfoResponse(
                club.getClubTier().name(),
                totalClubs,
                currentRank,
                ranksToNextTier
        );
    }



    @Transactional
    public void updateClubTiers() {
        List<SportsCategory> categories = Arrays.asList(SportsCategory.values());

        for (SportsCategory category : categories) {
            List<Club> clubs = clubRepository.findBySportsCategoryOrderByMatchScoreDesc(category);

            int totalClubs = clubs.size();
            int goldCutoff = (int) Math.ceil(totalClubs * 0.2);
            int silverCutoff = (int) Math.ceil(totalClubs * 0.5);

            for (int i = 0; i < totalClubs; i++) {
                Club club = clubs.get(i);
                if (i < goldCutoff) {
                    club.updateTier(ClubTier.GOLD);
                } else if (i < silverCutoff) {
                    club.updateTier(ClubTier.SILVER);
                } else {
                    club.updateTier(ClubTier.BRONZE);
                }
            }
            clubRepository.saveAll(clubs);
        }
    }


    @Transactional
    public List<ClubRankResponse> getClubRankingByCategory(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        SportsCategory category = club.getSportsCategory();
        List<Club> clubs = clubRepository.findBySportsCategoryOrderByMatchScoreDesc(category);

        List<ClubRankResponse> rankings = new ArrayList<>();
        for (int i = 0; i < clubs.size(); i++) {
            Club currentClub = clubs.get(i);
            int rankChange = currentClub.getClubRank() - (i + 1); // 순위 변동 계산
            ClubRankResponse response = new ClubRankResponse(
                    currentClub.getClubName(),
                    currentClub.getLogo(),
                    currentClub.getMatchScore(),
                    currentClub.getClubRank(),
                    rankChange
            );
            rankings.add(response);
        }

        return rankings;
    }



    public List<ClubRankResponse> getClubsByTierAndRank(SportsCategory sportsCategory) {
        List<Club> clubs = clubRepository.findBySportsCategoryOrderByMatchScoreDesc(sportsCategory);
        List<ClubRankResponse> response = new ArrayList<>();
        int rank = 1;

        for (Club club : clubs) {
            // 이전 순위와 현재 순위 간의 변동 계산
            int previousRank = club.getClubRank(); // 클럽의 이전 순위를 가져옵니다.
            int rankChange = previousRank - rank; // 순위 변동 계산

            response.add(new ClubRankResponse(club.getLogo(), club.getClubName(), club.getMatchScore(), rank, rankChange));

            // 현재 순위를 업데이트
            club.updateClubRank(rank);
            rank++;
        }

        // 클럽의 최신 순위를 업데이트하여 저장 (옵션)
        clubRepository.saveAll(clubs);

        return response;
    }


//    public List<ClubRankResponse> getMonthlyClubsByTierAndRank(SportsCategory sportsCategory, int year, int month) {
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//
//        // 특정 월 동안의 매치 점수를 기준으로 클럽들을 가져옵니다.
//        List<Club> clubs = clubRepository.findBySportsCategoryAndMatchDateBetweenOrderByMatchScoreDesc(sportsCategory, startDate, endDate);
//        List<ClubRankResponse> response = new ArrayList<>();
//        int rank = 1;
//
//        for (Club club : clubs) {
//            int previousRank = getPreviousRank(club, year, month - 1); // 이전 달의 순위를 가져오는 로직
//            int rankChange = previousRank - rank; // 순위 변동 계산
//
//            response.add(new ClubRankResponse(club.getLogo(), club.getClubName(), club.getMatchScore(), rank, rankChange));
//
//            // 현재 순위를 업데이트
//            club.updateClubRank(rank);
//            rank++;
//        }
//
//        // 클럽의 최신 순위를 업데이트하여 저장 (선택 사항)
//        clubRepository.saveAll(clubs);
//
//        return response;
//    }
//
//    // 이전 달의 순위를 가져오는 메서드 (예시)
//    private int getPreviousRank(Club club, int year, int month) {
//        // 이전 달의 시작일과 종료일을 계산
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//
//        // 이전 달의 데이터를 가져와서 순위를 계산하는 로직이 필요
//        List<Club> previousMonthClubs = clubRepository.findBySportsCategoryAndMatchDateBetweenOrderByMatchScoreDesc(club.getSportsCategory(), startDate, endDate);
//
//        // 클럽의 이전 달 순위를 계산하여 반환
//        for (int i = 0; i < previousMonthClubs.size(); i++) {
//            if (previousMonthClubs.get(i).getClubId().equals(club.getClubId())) {
//                return i + 1;
//            }
//        }
//        return -1; // 만약 이전 달 순위를 찾지 못한 경우
//    }

}
