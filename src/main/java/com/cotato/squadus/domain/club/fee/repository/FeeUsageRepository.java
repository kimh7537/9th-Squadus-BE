package com.cotato.squadus.domain.club.fee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.fee.entity.FeeType;
import com.cotato.squadus.domain.club.fee.entity.FeeUsage;

public interface FeeUsageRepository extends JpaRepository<FeeUsage, Long> {

	List<FeeUsage> findAllByFeeType(FeeType feeType);

	List<FeeUsage> findAllByFeeType_Club_ClubId(Long clubId);
}
