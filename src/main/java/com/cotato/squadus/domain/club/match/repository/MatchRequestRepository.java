package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.MatchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MatchRequestRepository extends JpaRepository<MatchRequest, Long> {


    List<MatchRequest> findByMatchPost(MatchPost matchPost);

    Page<MatchRequest> findAllByClub_ClubId(Long clubId, Pageable pageable);

    List<MatchRequest> findAllByClub_ClubId(Long clubId);

    Page<MatchRequest> findByMatchPost(MatchPost matchPost, Pageable pageable);

    @Query("SELECT mr FROM MatchRequest mr WHERE mr.matchPost.homeClub.clubId = :clubId")
    Page<MatchRequest> findAllReceivedRequests(@Param("clubId") Long clubId, Pageable pageable);

    @Query("SELECT mr FROM MatchRequest mr WHERE mr.matchPost.homeClub.clubId = :clubId")
    List<MatchRequest> findAllReceivedRequests(@Param("clubId") Long clubId);
}
