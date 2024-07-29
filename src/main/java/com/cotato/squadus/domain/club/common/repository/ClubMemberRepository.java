package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}