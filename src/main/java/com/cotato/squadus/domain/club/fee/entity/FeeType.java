package com.cotato.squadus.domain.club.fee.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.cotato.squadus.common.entity.BaseTimeEntity;
import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.common.entity.ClubMember;
import com.cotato.squadus.domain.club.fee.enums.FeeCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "fee_type")
public class FeeType extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long feeTypeId;

	private String feeTypeName;

	private Long price; // 인당 내야할 금액

	@Enumerated(EnumType.STRING)
	private FeeCategory feeCategory;

	private LocalDate startDate;

	private LocalDate endDate;

	private String memo;

	private Long totalPrice; // 납부 총 금액
	private Long balance; // 잔액

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	private Club club;

	@OneToMany(mappedBy = "feeType", fetch = LAZY, cascade = ALL)
	private List<FeePayment> feePayments = new ArrayList<>();

	@OneToMany(mappedBy = "feeType", fetch = LAZY, cascade = ALL)
	private List<FeeUsage> feeUsages = new ArrayList<>();

	public void updateFeePayments(FeePayment feePayment) {
		this.feePayments.add(feePayment);
	}

	public void updateFeeUsages(FeeUsage feeUsage) {
		this.feeUsages.add(feeUsage);
	}

	public void updateTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Builder
	public FeeType(Club club, String feeTypeName, Long price, String memo, List<ClubMember> clubMembers,
		FeeCategory feeCategory, LocalDate startDate, LocalDate endDate, Long totalPrice, Long balance) {
		this.club = club;
		this.feeTypeName = feeTypeName;
		this.price = price;
		this.memo = memo;
		this.feeCategory = feeCategory;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalPrice = totalPrice;
		this.balance = balance;
	}

	public void update(String feeTypeName, Long price, String memo, FeeCategory feeCategory, LocalDate startDate,
		LocalDate endDate) {
		if (feeTypeName != null)
			this.feeTypeName = feeTypeName;
		if (price != null)
			this.price = price;
		if (memo != null)
			this.memo = memo;
		if (feeCategory != null)
			this.feeCategory = feeCategory;
		if (startDate != null)
			this.startDate = startDate;
		if (endDate != null)
			this.endDate = endDate;
	}

	public void updateBalance(Long price) {
		this.balance += price;
	}
}
