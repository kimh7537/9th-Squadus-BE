package com.cotato.squadus.domain.club.post.repository;
import com.cotato.squadus.domain.club.post.entity.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

    Optional<ClubPost> findByPostId(Long clubId);

    List<ClubPost> findAllByClub_ClubId(Long clubId);
}
