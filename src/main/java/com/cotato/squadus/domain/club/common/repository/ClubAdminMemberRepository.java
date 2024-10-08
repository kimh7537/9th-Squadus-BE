package com.cotato.squadus.domain.club.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;

public interface ClubAdminMemberRepository extends JpaRepository<ClubAdminMember, Long> {
	Optional<ClubAdminMember> findByClubMemberIdxAndAdminStatus(Long id, AdminStatus status);

	@Query("SELECT cam FROM ClubAdminMember cam WHERE cam.club.clubId = :clubId AND cam.clubMemberIdx = :memberId AND cam.adminStatus = 'CURRENT'")
	Optional<ClubAdminMember> findActiveAdminByClubIdAndClubMemberId(@Param("clubId") Long clubId,
		@Param("memberId") Long memberId);

}
