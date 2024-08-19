package com.cotato.squadus.domain.club.fee.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
