package com.cotato.squadus.api.recruit.dto;

import java.util.List;

public record RecruitingPostResponseList(
        List<RecruitingPostResponse> recruitingPostResponseList
) {
    public static RecruitingPostResponseList from(List<RecruitingPostResponse> recruitingPostResponseList) {
        return new RecruitingPostResponseList(recruitingPostResponseList);
    }
}
