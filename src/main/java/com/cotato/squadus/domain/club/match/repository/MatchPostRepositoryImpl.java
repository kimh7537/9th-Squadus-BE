package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Tier;
import com.cotato.squadus.domain.club.common.enums.SportsCategory;
import com.cotato.squadus.domain.club.match.entity.MatchPost;
import com.cotato.squadus.domain.club.match.entity.QMatchPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cotato.squadus.domain.club.match.entity.QMatchPost.matchPost;

@Repository
@Slf4j
public class MatchPostRepositoryImpl implements MatchPostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MatchPostRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MatchPost> customFindMatchesByFilter(SportsCategory sportsCategory, String city, String district, Tier tier, Boolean placeProvided) {
        QMatchPost matchPost = QMatchPost.matchPost;
        return jpaQueryFactory
                .selectFrom(matchPost)
                .where(
                        sportsCategoryEq(sportsCategory),
                        cityEq(city),
                        districtEq(district),
                        tierEq(tier),
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
                        matchPost.title.likeIgnoreCase(pattern)
                                .or(matchPost.content.likeIgnoreCase(pattern))
                )
                .fetch();
    }

    private BooleanExpression sportsCategoryEq(SportsCategory sportsCategory) {
        return sportsCategory != null ? matchPost.sportsCategory.eq(sportsCategory) : null;
    }

    private BooleanExpression cityEq(String city) {
        return city != null ? matchPost.matchPlace.city.eq(city) : null;
    }

    private BooleanExpression districtEq(String district) {
        return district != null ? matchPost.matchPlace.district.eq(district) : null;
    }

    private BooleanExpression tierEq(Tier tier) {
        return tier != null ? matchPost.tier.eq(tier) : null;
    }

    private BooleanExpression placeProvidedEq(Boolean placeProvided) {
        return placeProvided != null ? matchPost.placeProvided.eq(placeProvided) : null;
    }
}
