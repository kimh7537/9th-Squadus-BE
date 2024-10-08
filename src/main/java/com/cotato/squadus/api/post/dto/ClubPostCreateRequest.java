package com.cotato.squadus.api.post.dto;

public record ClubPostCreateRequest(
	String title,
	String content,
	Boolean hasVote,
	Boolean isImportant
) {
}
