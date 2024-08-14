package com.cotato.squadus.domain.club.common.enums;

public enum ClubCategory {

    SCHOOL("교내"),
    UNION("연합");

    private final String description;

    ClubCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
