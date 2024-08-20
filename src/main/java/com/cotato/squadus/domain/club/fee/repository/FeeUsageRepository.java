package com.cotato.squadus.domain.club.fee.repository;

import com.cotato.squadus.domain.club.fee.entity.FeeType;
import com.cotato.squadus.domain.club.fee.entity.FeeUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeUsageRepository extends JpaRepository<FeeUsage, Long> {

    List<FeeUsage> findAllByFeeType(FeeType feeType);
}
