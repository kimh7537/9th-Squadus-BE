package com.cotato.squadus.domain.club.match.repository.mercenary;

import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MercenaryRequestRepository extends JpaRepository<MercenaryRequest, Long> {

    Page<MercenaryRequest> findAllByMember_MemberIdx(Member member, Pageable pageable);

    List<MercenaryRequest> findAllByMember_MemberIdx(Member member);

    Optional<MercenaryRequest> findTop1ByMemberAndMercenaryPost(Member member, MercenaryPost mercenaryPost);

//    Optional<MercenaryRequest> findTop1ByClubMemberAndMercenaryPost(ClubMember clubMember, MercenaryPost mercenaryPost);

}
