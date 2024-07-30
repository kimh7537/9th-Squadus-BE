package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.auth.enums.AdminStatus;
import com.cotato.squadus.domain.club.common.entity.ClubAdminMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubAdminMemberRepository extends JpaRepository<ClubAdminMember, Long> {
    Optional<ClubAdminMember> findByClubMemberIdxAndAdminStatus(Long id, AdminStatus status);
}
