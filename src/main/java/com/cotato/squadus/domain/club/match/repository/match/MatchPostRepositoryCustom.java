package com.cotato.squadus.domain.club.match.repository.match;

import java.util.List;

import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.match.MatchPost;

public interface MatchPostRepositoryCustom {

	List<MatchPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district,
		ClubTier tier, Boolean placeProvided);

	List<MatchPost> customFindByKeyword(String keyword);

}
