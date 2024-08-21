package com.cotato.squadus.domain.club.match.repository.match;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.match.MatchPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MatchPostRepository extends JpaRepository<MatchPost, Long>, MatchPostRepositoryCustom {


    @Query("SELECT m FROM MatchPost m WHERE m.matchStartDate > :date " +
            "OR (m.matchStartDate = :date AND m.matchStartTime > :time)")
    Page<MatchPost> findAllByMatchStartDateAfterOrMatchStartDateEqualsAndMatchStartTimeAfter(
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            Pageable pageable);

    List<MatchPost> findByHomeClub(Club club);
}
