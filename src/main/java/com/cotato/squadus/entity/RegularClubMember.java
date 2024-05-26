package com.cotato.squadus.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("MEMBER")
public class RegularClubMember extends ClubMember {

}
