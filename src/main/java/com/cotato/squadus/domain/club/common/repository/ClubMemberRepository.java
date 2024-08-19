package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Optional<ClubMember> findClubMemberByClubMemberIdx(Long idx);
    Optional<ClubMember> findClubMemberByMember_MemberIdxAndClub_ClubId(Long memberIdx, Long clubId);

    List<ClubMember> findAllByClub_ClubId(Long clubId);

}