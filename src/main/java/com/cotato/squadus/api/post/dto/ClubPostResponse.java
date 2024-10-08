package com.cotato.squadus.api.post.dto;

import java.time.LocalDateTime;

import com.cotato.squadus.domain.club.post.entity.ClubPost;

public record ClubPostResponse(
	Long postId,
	String title,
	String content,
	String image,
	LocalDateTime createdAt,
	Long view,
	Long likes
) {

	public static ClubPostResponse from(ClubPost clubPost) {
		return new ClubPostResponse(
			clubPost.getPostId(),
			clubPost.getTitle(),
			clubPost.getContent(),
			clubPost.getImage(),
			clubPost.getCreatedAt(),
			clubPost.getViews(),
			clubPost.getLikes()
		);
	}
}