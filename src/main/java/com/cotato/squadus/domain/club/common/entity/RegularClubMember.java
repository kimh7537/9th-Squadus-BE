package com.cotato.squadus.domain.club.common.entity;

import com.cotato.squadus.domain.auth.enums.MemberStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("MEMBER")
public class RegularClubMember extends ClubMember {

}
