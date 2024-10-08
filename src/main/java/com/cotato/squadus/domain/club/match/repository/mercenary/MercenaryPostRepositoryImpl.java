package com.cotato.squadus.domain.club.match.repository.mercenary;

import static com.cotato.squadus.domain.club.match.entity.mercenary.QMercenaryPost.*;
import static com.querydsl.core.types.dsl.Expressions.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.mercenary.MercenaryPost;
import com.cotato.squadus.domain.club.match.entity.mercenary.QMercenaryPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class MercenaryPostRepositoryImpl implements MercenaryPostRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public MercenaryPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<MercenaryPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district,
		ClubTier clubTier, Boolean placeProvided) {
		QMercenaryPost mercenaryPost = QMercenaryPost.mercenaryPost;
		return jpaQueryFactory
			.selectFrom(mercenaryPost)
			.where(
				sportsCategoryEq(sportsCategory),
				cityEq(city),
				districtEq(district),
				tierEq(clubTier),
				placeProvidedEq(placeProvided)
			)
			.fetch();
	}

	@Override
	public List<MercenaryPost> customFindByKeyword(String keyword) {
		QMercenaryPost mercenaryPost = QMercenaryPost.mercenaryPost;
		String pattern = "%" + keyword + "%"; // 부분 일치 검색을 위해 패턴 설정
		return jpaQueryFactory
			.selectFrom(mercenaryPost)
			.where(
				stringTemplate("cast({0} as string)", mercenaryPost.title)
					.likeIgnoreCase(pattern)
					.or(
						stringTemplate("cast({0} as string)", mercenaryPost.content)
							.likeIgnoreCase(pattern)
					)
			)
			.fetch();
	}

	private BooleanExpression sportsCategoryEq(SportsCategory sportsCategory) {
		return sportsCategory != null ? mercenaryPost.homeClub.sportsCategory.stringValue().eq(sportsCategory.name()) :
			null;
	}

	private BooleanExpression tierEq(ClubTier clubTier) {
		return clubTier != null ? mercenaryPost.homeClub.clubTier.stringValue().eq(clubTier.name()) : null;
	}

	private BooleanExpression cityEq(String city) {
		return city != null ? mercenaryPost.matchPlace.city.eq(city) : null;
	}

	private BooleanExpression districtEq(String district) {
		return district != null ? mercenaryPost.matchPlace.district.eq(district) : null;
	}

	private BooleanExpression placeProvidedEq(Boolean placeProvided) {
		return placeProvided != null ? mercenaryPost.placeProvided.eq(placeProvided) : null;
	}

}
