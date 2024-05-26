package com.cotato.squadus.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("ADMIN")
public class ClubAdminMember extends ClubMember {

    @Enumerated(EnumType.STRING)
    private AdminStatus adminStatus;

}
