package com.cotato.squadus.domain.club.match.repository.match;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchRequest;

public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {

	Page<MatchRequest> findAllByClub_ClubId(Long clubId, Pageable pageable);

	List<MatchRequest> findAllByClub_ClubId(Long clubId);

	Optional<MatchRequest> findTop1ByClubAndMatchPost(Club club, MatchPost matchPost);

}
