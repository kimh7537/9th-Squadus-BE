package com.cotato.squadus.domain.club.common.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

	private String city; // 시/도

	private String district; // 군/구

	@Builder
	public Region(String city, String district) {
		this.city = city;
		this.district = district;
	}
}
