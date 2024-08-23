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
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;
import com.cotato.squadus.domain.club.recruit.repository.RecruitingPostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ClubMemberService clubMemberService;
    private final ClubAdminService clubAdminService;
    private final MemberService memberService;
    private final S3ImageService s3ImageService;
    private final RecruitingPostRepository recruitingPostRepository;

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


        updateClubScore(club.getSportsCategory(), club.getClubId(), club.getMatchScore());

        log.info("동아리 생성됨, clubId : {}", savedClub.getClubId());
        return new ClubCreateResponse(savedClub.getClubId());
    }

    @Transactional
    public ClubApplyResponse joinClub(CustomOAuth2Member customOAuth2Member, Long clubId, ClubApplyRequest clubApplyRequest) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 동아리를 찾을 수 없습니다."));
        Member member = memberService.findMemberByUniqueId(customOAuth2Member.getUniqueId());

        RecruitingPost recruitingPost = recruitingPostRepository.findById(clubApplyRequest.getRecruitingPostId())
                .orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 홍보 게시글을 찾을 수 없습니다."));

        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .appliedAt(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .questions(recruitingPost.getQuestions())
                .answers(clubApplyRequest.getAnswers())
                .recruitingPost(recruitingPost)
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


    public void updateClubScore(SportsCategory category, Long clubId, int matchScore) {
        String key = "ranking:" + category.name();
        redisTemplate.opsForZSet().add(key, clubId.toString(), matchScore);
    }

    @Transactional
    public void updateClubMatchScore(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다."));

        // Redis에 업데이트
        updateClubScore(club.getSportsCategory(), club.getClubId(), club.getMatchScore());
    }


    public List<ClubRankResponse> getRanking(SportsCategory category) {
        String key = "ranking:" + category.name();
        Set<String> clubIds = redisTemplate.opsForZSet().reverseRange(key, 0, -1); // 높은 점수 순으로 가져옴

        // 클럽이 5개 미만이라면 티어 업데이트를 하지 않고 BRONZE로 유지
        if (clubIds.size() < 5) {
            return clubIds.stream()
                    .map(clubId -> clubRepository.findById(Long.parseLong(clubId))
                            .map(club -> new ClubRankResponse(club.getClubName(), club.getLogo(), club.getMatchScore(), club.getClubRank(), 0))
                            .orElseThrow(() -> new EntityNotFoundException("동아리를 찾을 수 없습니다.")))
                    .collect(Collectors.toList());
        }

        List<ClubRankResponse> rankingResponses = new ArrayList<>();
        int rank = 1;

        List<Club> clubs = clubIds.stream()
                .map(Long::parseLong)
                .map(clubRepository::findById)
                .map(Optional::get)
                .sorted(Comparator.comparingInt(Club::getMatchScore).reversed() // 먼저 점수 기준으로 정렬
                        .thenComparing(Club::getClubId)) // 점수가 같으면 clubId 기준으로 정렬
                .collect(Collectors.toList());

        int totalClubs = clubs.size();
        int goldCutoff = (int) Math.ceil(totalClubs * 0.2);
        int silverCutoff = (int) Math.ceil(totalClubs * 0.5);

        for (Club club : clubs) {
            // 기존 랭킹 정보와 비교하여 순위 변동 계산
            int previousRank = club.getClubRank() != null ? club.getClubRank() : rank;
            int rankChange = previousRank - rank;

            // ClubRankResponse로 변환
            ClubRankResponse response = new ClubRankResponse(
                    club.getClubName(),
                    club.getLogo(),
                    club.getMatchScore(),
                    rank,
                    rankChange
            );

            // 현재 랭킹을 Club 엔티티에 저장
            club.updateClubRank(rank);

            // 티어 갱신
            if (rank <= goldCutoff) {
                club.updateTier(ClubTier.GOLD);
            } else if (rank <= silverCutoff) {
                club.updateTier(ClubTier.SILVER);
            } else {
                club.updateTier(ClubTier.BRONZE);
            }

            rankingResponses.add(response);
            rank++;
        }

        clubRepository.saveAll(clubs);

        return rankingResponses;
    }

}
