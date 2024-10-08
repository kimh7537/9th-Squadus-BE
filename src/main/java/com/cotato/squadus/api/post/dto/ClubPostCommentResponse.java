package com.cotato.squadus.api.post.dto;

import java.time.LocalDateTime;

import com.cotato.squadus.domain.club.post.entity.ClubPostComment;

public record ClubPostCommentResponse(
	Long id,
	String clubMemberName,
	String content,
	LocalDateTime createdDate,
	String profileImgUrl,
	Long likes

) {

	public static ClubPostCommentResponse from(ClubPostComment clubPostComment) {
		return new ClubPostCommentResponse(
			clubPostComment.getId(),
			clubPostComment.getClubMember().getMember().getUsername(),
			clubPostComment.getContent(),
			clubPostComment.getCreatedAt(),
			clubPostComment.getClubMember().getMember().getProfileImage(),
			clubPostComment.getLikes()
		);
	}
}
