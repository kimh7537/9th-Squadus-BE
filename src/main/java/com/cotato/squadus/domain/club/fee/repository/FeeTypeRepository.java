package com.cotato.squadus.domain.club.fee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.fee.entity.FeeType;

public interface FeeTypeRepository extends JpaRepository<FeeType, Long> {

	List<FeeType> findAllByClub_ClubId(Long clubId);
}
