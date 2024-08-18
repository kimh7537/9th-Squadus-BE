package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchPostRepository extends JpaRepository<MatchPost, Long>, MatchPostRepositoryCustom {

    Page<MatchPost> findAllBy(Pageable pageable);

    List<MatchPost> findByHomeClub(Club club);
}
