package com.cotato.squadus.domain.club.match.repository.mercenary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.auth.entity.Member;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryRequest;

public interface MercenaryRequestRepository extends JpaRepository<MercenaryRequest, Long> {

	Page<MercenaryRequest> findAllByMember_MemberIdx(Member member, Pageable pageable);

	List<MercenaryRequest> findAllByMember_MemberIdx(Member member);

	Optional<MercenaryRequest> findTop1ByMemberAndMercenaryPost(Member member, MercenaryPost mercenaryPost);

	//    Optional<MercenaryRequest> findTop1ByClubMemberAndMercenaryPost(ClubMember clubMember, MercenaryPost mercenaryPost);

}
