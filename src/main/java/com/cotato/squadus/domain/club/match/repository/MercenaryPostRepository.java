package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.MercenaryPost;
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
public interface MercenaryPostRepository extends JpaRepository<MercenaryPost, Long>, MercenaryPostRepositoryCustom {

    @Query("SELECT m FROM MercenaryPost m WHERE m.homeClub <> :homeClub " +
            "AND (m.matchStartDate > :date " +
            "OR (m.matchStartDate = :date AND m.matchStartTime > :time))")
    Page<MercenaryPost> findAllByHomeClubNotAndMatchStartDateAfterOrMatchStartDateEqualsAndMatchStartTimeAfter(
            @Param("homeClub") Club homeClub,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            Pageable pageable);


    List<MercenaryPost> findByHomeClub(Club club);

}
