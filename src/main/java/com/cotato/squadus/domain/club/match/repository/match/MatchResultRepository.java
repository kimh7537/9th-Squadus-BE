package com.cotato.squadus.domain.club.match.repository.match;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchResult;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

	List<MatchResult> findByMatchPost(MatchPost matchPost);

}
