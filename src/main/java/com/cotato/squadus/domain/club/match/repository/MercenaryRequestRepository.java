package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.match.entity.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.MercenaryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MercenaryRequestRepository extends JpaRepository<MercenaryRequest, Long> {

    Page<MercenaryRequest> findAllByClubMember_ClubMemberIdx(Long clubMemberIdx, Pageable pageable);

    List<MercenaryRequest> findAllByClubMember_ClubMemberIdx(Long clubMemberIdx);

    Optional<MercenaryRequest> findTop1ByClubMemberAndMercenaryPost(ClubMember clubMember, MercenaryPost mercenaryPost);

}
