package com.cotato.squadus.api.post.dto;
import com.cotato.squadus.domain.club.post.entity.ClubPostComment;

import java.time.LocalDateTime;

public record ClubPostCommentResponse(
        Long id,
        String clubMemberName,
        String content,
        LocalDateTime createdDate,
        String profileImgUrl

) {

    public static ClubPostCommentResponse from(ClubPostComment clubPostComment) {
        return new ClubPostCommentResponse(
                clubPostComment.getId(),
                clubPostComment.getClubMember().getMember().getUsername(),
                clubPostComment.getContent(),
                clubPostComment.getCreatedAt(),
                clubPostComment.getClubMember().getClubProfileImage()
        );
    }
}
