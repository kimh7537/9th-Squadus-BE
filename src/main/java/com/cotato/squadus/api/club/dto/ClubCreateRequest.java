package com.cotato.squadus.api.club.dto;

import java.util.List;

import com.cotato.squadus.domain.club.common.enums.ClubCategory;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubCreateRequest {

	private String clubName;

	private ClubCategory clubCategory;

	private SportsCategory sportsCategory;

	private Long maxMembers;

	private String clubMessage;

	private List<String> tags;

	private String city;

	private String district;
}
