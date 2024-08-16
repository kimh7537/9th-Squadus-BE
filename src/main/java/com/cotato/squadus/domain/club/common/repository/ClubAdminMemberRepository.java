package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubAdminMemberRepository extends JpaRepository<ClubAdminMember, Long> {
    Optional<ClubAdminMember> findByClubMemberIdxAndAdminStatus(Long id, AdminStatus status);

    @Query("SELECT cam FROM ClubAdminMember cam WHERE cam.club.clubId = :clubId AND cam.member.memberIdx = :memberId AND cam.adminStatus = 'CURRENT'")
    Optional<ClubAdminMember> findActiveAdminByClubIdAndMemberId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);

}
