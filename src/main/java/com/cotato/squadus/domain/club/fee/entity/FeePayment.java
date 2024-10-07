package com.cotato.squadus.domain.club.fee.entity;

import com.cotato.squadus.domain.club.common.entity.ClubMember;

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
@Table(name = "fee_payment")
public class FeePayment {

	@Id
	@GeneratedValue
	private Long feePaymentId;

	private Boolean isPaid;

	@ManyToOne
	@JoinColumn(name = "fee_type_id")
	private FeeType feeType;

	@ManyToOne
	@JoinColumn(name = "club_member_idx")
	private ClubMember clubMember;

	public void updateIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	@Builder
	public FeePayment(Boolean isPaid, FeeType feeType, ClubMember clubMember) {
		this.isPaid = isPaid;
		this.feeType = feeType;
		this.clubMember = clubMember;
	}
}
