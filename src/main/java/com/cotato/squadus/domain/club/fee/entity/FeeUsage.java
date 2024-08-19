package com.cotato.squadus.domain.club.fee.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "fee_usage")
public class FeeUsage {

    @Id
    @GeneratedValue
    private Long feeUsageId;

    private String description; // 사용 내역

    private LocalDateTime used_at;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;
}
