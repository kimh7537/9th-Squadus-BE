package com.cotato.squadus.api.club.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubApplyRequest {

	private Long recruitingPostId;

	private Map<Integer, String> answers;
}
