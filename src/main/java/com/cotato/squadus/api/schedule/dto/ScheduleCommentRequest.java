package com.cotato.squadus.api.schedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleCommentRequest {
    private Long memberId;
    private String content;
}
