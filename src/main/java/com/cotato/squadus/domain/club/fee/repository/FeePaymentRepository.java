package com.cotato.squadus.domain.club.fee.repository;


import com.cotato.squadus.domain.club.fee.entity.FeePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
}
