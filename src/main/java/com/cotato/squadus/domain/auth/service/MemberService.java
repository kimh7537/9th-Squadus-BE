package com.cotato.squadus.domain.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.squadus.api.member.dto.MemberClubApplicationInfoResponse;
import com.cotato.squadus.api.member.dto.MemberClubApplicationListResponse;
import com.cotato.squadus.api.member.dto.MemberClubListResponse;
import com.cotato.squadus.api.member.dto.MemberClubResponse;
import com.cotato.squadus.api.member.dto.MemberInfoResponse;
import com.cotato.squadus.common.config.auth.CustomOAuth2Member;
import com.cotato.squadus.common.config.jwt.JWTUtil;
import com.cotato.squadus.common.s3.S3ImageService;
import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.auth.repository.MemberRepository;
import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.common.repository.ClubApplicationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JWTUtil jwtUtil;
	private final S3ImageService s3ImageService;
	private final ClubApplicationRepository clubApplicationRepository;

	@Transactional
	public Long saveMember(Member member) {
		Member save = memberRepository.save(member);
		return save.getMemberIdx();
	}

	public MemberInfoResponse findMemberInfo(CustomOAuth2Member customOAuth2Member) {
		Member findMember = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
			.orElseThrow(() -> new EntityNotFoundException("해당 고유번호를 가진 회원을 찾을 수 없습니다."));
		return MemberInfoResponse.from(findMember);
	}

	public Member findMemberByUniqueId(String uniqueId) {
		Member member = memberRepository.findByUniqueId(uniqueId)
			.orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));
		return member;
	}

	public MemberClubListResponse findJoinedClubs(CustomOAuth2Member customOAuth2Member) {
		Member member = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
			.orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));
		List<ClubMember> clubMemberships = member.getClubMemberships();
		List<MemberClubResponse> memberClubResponseList = clubMemberships.stream()
			.map(MemberClubResponse::from)
			.toList();

		return MemberClubListResponse.from(memberClubResponseList);
	}

	@Transactional
	public MemberInfoResponse updateProfileImage(CustomOAuth2Member customOAuth2Member,
		MultipartFile profileImageFile) {
		String profileImage = s3ImageService.upload(profileImageFile);

		Member member = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
			.orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원을 찾을 수 없습니다."));
		Member updatedMember = member.updateProfileImage(profileImage);
		return MemberInfoResponse.from(updatedMember);
	}

	@Transactional
	public MemberInfoResponse deleteProfileImage(CustomOAuth2Member customOAuth2Member) {
		Member member = findMemberByUniqueId(customOAuth2Member.getUniqueId());
		s3ImageService.deleteImageFromS3(member.getProfileImage());
		member.updateProfileImage("default profile img");
		memberRepository.save(member);
		return MemberInfoResponse.from(member);
	}

	public MemberClubApplicationListResponse findAppliedClubs(CustomOAuth2Member customOAuth2Member) {
		Member member = memberRepository.findByUniqueId(customOAuth2Member.getUniqueId())
			.orElseThrow(() -> new EntityNotFoundException("해당 uniqueId를 가진 회원이 존재하지 않습니다."));

		List<ClubApplication> clubApplications = clubApplicationRepository.findByMember_MemberIdxFetchClubAndRecruitingPost(
			member.getMemberIdx());

		List<MemberClubApplicationInfoResponse> memberClubApplicationInfoResponses = clubApplications.stream()
			.map(MemberClubApplicationInfoResponse::from)
			.toList();

		return MemberClubApplicationListResponse.from(memberClubApplicationInfoResponses);
	}
}
