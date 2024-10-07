package com.cotato.squadus.domain.auth.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.squadus.api.club.dto.ClubMemberInfoResponse;
import com.cotato.squadus.api.club.dto.ClubMemberInfoResponseList;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.error.ErrorCode;
import com.cotato.squadus.common.error.exception.AppException;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubMemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubMemberService {

	private final ClubMemberRepository clubMemberRepository;
	private final MemberService memberService;

	@Transactional
	public ClubMember saveClubMember(ClubMember clubMember) {
		ClubMember savedClubMember = clubMemberRepository.save(clubMember);
		return savedClubMember;
	}

	public ClubMember findClubMemberById(Long id) {
		ClubMember clubMember = clubMemberRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 회원을 찾을 수 없습니다."));
		return clubMember;
	}

	// 임시 세션 정보를 통해 회원이 속한 동아리인지 검증
	public void validateClubMember(Long clubId) {
		ClubMember clubMember = findClubMemberBySecurityContextHolder(clubId);
		log.info("현재 로그인 된 회원의 clubId: {} ", clubMember.getClub().getClubId());
		if (!clubMember.getClub().getClubId().equals(clubId)) {
			throw new AppException(ErrorCode.CLUB_ACCESS_DENIED);
		}
	}

	public ClubMember findClubMemberBySecurityContextHolder(Long clubId) {
		CustomOAuth2Member oAuth2Member = (CustomOAuth2Member)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
		Member member = memberService.findMemberByUniqueId(oAuth2Member.getUniqueId());
		ClubMember clubMember = clubMemberRepository.findClubMemberByMember_MemberIdxAndClub_ClubId(
				member.getMemberIdx(), clubId)
			.orElseThrow(() -> new EntityNotFoundException("해당 회원 고유번호를 가진 동아리 회원을 찾을 수 없습니다."));
		return clubMember;
	}

	public ClubMemberInfoResponseList findAllClubMemberInfo(Long clubId) {
		List<ClubMemberInfoResponse> clubMemberInfoResponseList = clubMemberRepository.findAllByClub_ClubId(clubId)
			.stream()
			.map(ClubMemberInfoResponse::from)
			.toList();

		return ClubMemberInfoResponseList.from(clubMemberInfoResponseList);

	}

}
