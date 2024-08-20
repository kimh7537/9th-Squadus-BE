package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.ClubTier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.QMatchPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cotato.squadus.domain.club.match.entity.QMatchPost.matchPost;
import static com.cotato.squadus.domain.club.match.entity.QMercenaryPost.mercenaryPost;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;

@Repository
@Slf4j
public class MatchPostRepositoryImpl implements MatchPostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MatchPostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MatchPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district, ClubTier clubTier, Boolean placeProvided) {
        QMatchPost matchPost = QMatchPost.matchPost;
        return jpaQueryFactory
                .selectFrom(matchPost)
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
    public List<MatchPost> customFindByKeyword(String keyword) {
        QMatchPost matchPost = QMatchPost.matchPost;
        String pattern = "%" + keyword + "%"; // 부분 일치 검색을 위해 패턴 설정
        return jpaQueryFactory
                .selectFrom(matchPost)
                .where(
                        stringTemplate("cast({0} as string)", matchPost.title)
                                .likeIgnoreCase(pattern)
                                .or(
                                        stringTemplate("cast({0} as string)", matchPost.content)
                                                .likeIgnoreCase(pattern)
                                )
                )
                .fetch();
    }

    private BooleanExpression sportsCategoryEq(SportsCategory sportsCategory) {
        return sportsCategory != null ? matchPost.homeClub.sportsCategory.stringValue().eq(sportsCategory.name()) : null;
    }

    private BooleanExpression tierEq(ClubTier clubTier) {
        return clubTier != null ? matchPost.homeClub.clubTier.stringValue().eq(clubTier.name()) : null;
    }

    private BooleanExpression cityEq(String city) {
        return city != null ? matchPost.matchPlace.city.eq(city) : null;
    }

    private BooleanExpression districtEq(String district) {
        return district != null ? matchPost.matchPlace.district.eq(district) : null;
    }

    private BooleanExpression placeProvidedEq(Boolean placeProvided) {
        return placeProvided != null ? matchPost.placeProvided.eq(placeProvided) : null;
    }
}
