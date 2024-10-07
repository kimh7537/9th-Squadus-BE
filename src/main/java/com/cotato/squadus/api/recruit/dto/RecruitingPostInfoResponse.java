package com.cotato.squadus.api.recruit.dto;

import java.util.Map;

import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;

public record RecruitingPostInfoResponse(
	Long recruitingPostId,
	SportsCategory sportsCategory,
	Map<Integer, String> questions
) {

	public static RecruitingPostInfoResponse from(RecruitingPost recruitingPost) {
		return new RecruitingPostInfoResponse(
			recruitingPost.getPostId(),
			recruitingPost.getClub().getSportsCategory(),
			recruitingPost.getQuestions()
		);
	}

}
