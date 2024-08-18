package com.cotato.squadus.domain.club.match.repository;

import com.cotato.squadus.domain.club.common.entity.Club;
import com.cotato.squadus.domain.club.match.entity.MercenaryPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MercenaryPostRepository extends JpaRepository<MercenaryPost, Long>, MercenaryPostRepositoryCustom {

    Page<MercenaryPost> findAllByHomeClubNot(Club club, Pageable pageable);

    List<MercenaryPost> findByHomeClub(Club club);

}
