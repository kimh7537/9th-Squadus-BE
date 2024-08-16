package com.cotato.squadus.domain.club.common.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SportsCategory {
    BALL("구기", Arrays.asList("축구", "농구", "배구", "야구")),
    RACKET("라켓", Arrays.asList("테니스", "배드민턴", "탁구")),
    MARTIAL_ARTS("격투", Arrays.asList("태권도", "유도", "검도")),
    TRACK_AND_FIELD("육상", Arrays.asList("러닝", "크로스핏", "사이클")),
    WATER_SPORTS("수상", Arrays.asList("수영", "서핑", "요트")),
    OTHER("기타", Collections.emptyList());

    private final String name;
    private final List<String> subCategories;

    SportsCategory(String name, List<String> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }
}
