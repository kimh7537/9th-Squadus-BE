package com.cotato.squadus.domain.club.fee.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "fee_usage")
public class FeeUsage {

	@Id
	@GeneratedValue
	private Long feeUsageId;

	private String description; // 사용 내역

	private LocalDate usedAt;

	private Long price;

	@ManyToOne
	@JoinColumn(name = "fee_type_id")
	private FeeType feeType;

	@Builder
	public FeeUsage(String description, LocalDate usedAt, Long price, FeeType feeType) {
		this.feeType = feeType;
		this.description = description;
		this.usedAt = usedAt;
		this.price = price;
	}
}
