package com.cotato.squadus.common.config.jwt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uniqueId;
    private String username;

    @Column(nullable = false, length = 2048)
    private String refresh;
    private String expiration;
}
