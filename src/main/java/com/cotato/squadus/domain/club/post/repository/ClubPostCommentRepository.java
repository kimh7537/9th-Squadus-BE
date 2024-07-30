package com.cotato.squadus.domain.club.post.repository;

import com.cotato.squadus.domain.club.post.entity.ClubPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPostCommentRepository extends JpaRepository<ClubPostComment, Long> {

    List<ClubPostComment>findAllByClubPost_PostId(Long postId);
}
