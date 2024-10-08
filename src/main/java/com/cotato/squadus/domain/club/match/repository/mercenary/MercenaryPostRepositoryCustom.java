package com.cotato.squadus.domain.club.match.repository.mercenary;

import java.util.List;

import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;

public interface MercenaryPostRepositoryCustom {

	List<MercenaryPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district,
		ClubTier tier, Boolean placeProvided);

	List<MercenaryPost> customFindByKeyword(String keyword);

}
