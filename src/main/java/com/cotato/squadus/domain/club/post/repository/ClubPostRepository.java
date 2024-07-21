package com.cotato.squadus.domain.club.post.repository;
import com.cotato.squadus.domain.club.post.entity.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

    List<ClubPost> findAllByClub_ClubIdx(Long clubIdx);
}
