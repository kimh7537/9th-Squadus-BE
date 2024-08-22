package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {

    List<ClubApplication> findByRecruitingPost_PostId(Long postId);
}
