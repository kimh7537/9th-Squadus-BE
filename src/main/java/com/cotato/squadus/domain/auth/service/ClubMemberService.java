package com.cotato.squadus.domain.auth.service;

import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;
    private final MemberService memberService;


    // 임시 세션 정보를 통해 회원이 속한 동아리인지 검증
    public void validateClubMember(Long clubId) {
        ClubMember clubMember = findClubMemberBySecurityContextHolder();
        log.info("현재 로그인 된 회원의 clubId: {} ", clubMember.getClub().getClubId());
        if(!clubMember.getClub().getClubId().equals(clubId)) {
            throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
        }
    }

     private ClubMember findClubMemberBySecurityContextHolder() {
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberService.findMemberByUniqueId(oAuth2Member.getUniqueId());
        ClubMember clubMember = clubMemberRepository.findClubMemberByMember_MemberIdx(member.getMemberIdx())
                .orElseThrow(() -> new EntityNotFoundException("해당 회원 고유번호를 가진 동아리 회원을 찾을 수 없습니다."));
        return clubMember;
    }

}
