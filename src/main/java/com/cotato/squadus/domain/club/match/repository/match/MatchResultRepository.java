package com.cotato.squadus.domain.club.match.repository.match;

import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import com.cotato.squadus.domain.club.match.entity.match.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    List<MatchResult> findByMatchPost(MatchPost matchPost);

}
