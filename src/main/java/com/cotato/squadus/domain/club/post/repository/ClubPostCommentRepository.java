package com.cotato.squadus.domain.club.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.squadus.domain.club.post.entity.ClubPostComment;

@Repository
public interface ClubPostCommentRepository extends JpaRepository<ClubPostComment, Long> {

	List<ClubPostComment> findAllByClubPost_PostId(Long postId);
}
