package com.cotato.squadus.api.recruit.dto;

import java.time.LocalDate;
import java.util.List;

public record RecruitingPostCreateRequest(
        Long clubId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        List<String> questions
) {
}
