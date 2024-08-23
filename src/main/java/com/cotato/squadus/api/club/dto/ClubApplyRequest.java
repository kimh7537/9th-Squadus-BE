package com.cotato.squadus.api.club.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ClubApplyRequest {

    private Long recruitingPostId;

    private Map<Integer, String> answers;
}
