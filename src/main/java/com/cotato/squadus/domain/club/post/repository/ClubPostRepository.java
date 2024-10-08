package com.cotato.squadus.domain.club.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.squadus.domain.club.post.entity.ClubPost;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

	Optional<ClubPost> findByPostId(Long clubId);

	List<ClubPost> findAllByClub_ClubId(Long clubId);
}
