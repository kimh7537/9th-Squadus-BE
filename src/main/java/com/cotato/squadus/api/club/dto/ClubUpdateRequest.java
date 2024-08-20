package com.cotato.squadus.api.club.dto;

public record ClubUpdateRequest(
        String logo,
        String clubMessage,
        String city,
        String district,
        Long maxMembers
) {

}
