package com.cotato.squadus.domain.club.common.repository;

import com.cotato.squadus.domain.club.common.entity.ClubApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {

    List<ClubApplication> findByRecruitingPost_PostId(Long postId);

    List<ClubApplication> findByMember_MemberIdx(Long memberIdx);

    @Query("SELECT ca FROM ClubApplication ca " +
        "JOIN FETCH ca.recruitingPost rp " +
        "JOIN FETCH rp.club c " +
        "WHERE ca.member.memberIdx = :memberIdx")
    List<ClubApplication> findByMember_MemberIdxFetchClubAndRecruitingPost(@Param("memberIdx") Long memberIdx);
}
