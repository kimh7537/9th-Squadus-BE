package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.MatchPost;

import java.util.List;

public interface MatchPostRepositoryCustom {

    List<MatchPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district, Tier tier, Boolean placeProvided);

    List<MatchPost> customFindByKeyword(String keyword);

}
