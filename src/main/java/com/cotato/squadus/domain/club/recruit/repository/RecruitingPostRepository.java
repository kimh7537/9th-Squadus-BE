package com.cotato.squadus.domain.club.recruit.repository;

import com.cotato.squadus.domain.club.recruit.entity.RecruitingPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long> {

    List<RecruitingPost> findAllByClub_ClubId(Long clubId);
}
