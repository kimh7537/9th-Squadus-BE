package com.cotato.squadus.api.recruit.dto;

import java.time.LocalDate;
import java.util.Map;

public record RecruitingPostCreateRequest(
	Long clubId,
	String title,
	LocalDate startDate,
	LocalDate endDate,
	Map<Integer, String> questions
) {
}
