package com.cotato.squadus.domain.club.common.entity;

import com.cotato.squadus.domain.auth.enums.AdminStatus;
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
