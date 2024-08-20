package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findBySportsCategoryOrderByMatchScoreDesc(SportsCategory sportsCategory);

    List<Club> findBySportsCategoryAndMatchDateBetweenOrderByMatchScoreDesc(SportsCategory sportsCategory, LocalDate startDate, LocalDate endDate);

}
