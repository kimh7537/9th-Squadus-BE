package com.cotato.squadus.domain.club.recruit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;

public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long> {

	List<RecruitingPost> findAllByClub_ClubId(Long clubId);

	@Query("SELECT rp FROM RecruitingPost rp JOIN FETCH rp.club")
	Page<RecruitingPost> findAllWithClub(Pageable pageable);
}
