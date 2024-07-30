package com.cotato.squadus.domain.club.post.repository;

import com.cotato.squadus.domain.club.post.entity.ClubPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostCommentRepository extends JpaRepository<ClubPostComment, Long> {


}
