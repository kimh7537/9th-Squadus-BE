package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Optional<ClubMember> findClubMemberByMember_MemberIdx(Long idx);
}