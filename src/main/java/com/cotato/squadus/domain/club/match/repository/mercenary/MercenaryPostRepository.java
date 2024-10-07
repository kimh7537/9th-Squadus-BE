package com.cotato.squadus.domain.club.match.repository.mercenary;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;

@Repository
public interface MercenaryPostRepository extends JpaRepository<MercenaryPost, Long>, MercenaryPostRepositoryCustom {

	@Query("SELECT m FROM MercenaryPost m WHERE m.matchStartDate > :date " +
		"OR (m.matchStartDate = :date AND m.matchStartTime > :time)")
	Page<MercenaryPost> findAllByMatchStartDateAfterOrMatchStartDateEqualsAndMatchStartTimeAfter(
		@Param("date") LocalDate date,
		@Param("time") LocalTime time,
		Pageable pageable);

	List<MercenaryPost> findByHomeClub(Club club);

}
