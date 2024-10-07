package com.cotato.squadus.domain.club.fee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.club.fee.entity.FeePayment;

public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {

	List<FeePayment> findAllByFeeType_FeeTypeId(Long feeTypeId);

}
