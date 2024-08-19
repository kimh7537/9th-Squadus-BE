package com.cotato.squadus.domain.club.fee.repository;

import com.cotato.squadus.domain.club.fee.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeTypeRepository extends JpaRepository<FeeType, Long> {

    List<FeeType> findAllByClub_ClubId(Long clubId);
}
