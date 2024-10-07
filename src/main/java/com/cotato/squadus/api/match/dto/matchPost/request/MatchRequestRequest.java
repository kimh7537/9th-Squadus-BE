package com.cotato.squadus.api.match.dto.matchPost.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchRequestRequest {
	private Long clubMemberId;
	private Long matchPostId;
}