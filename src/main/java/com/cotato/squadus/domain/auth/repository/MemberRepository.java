package com.cotato.squadus.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.squadus.domain.auth.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Boolean existsByUsername(String username);

	Optional<Member> findByUsername(String username);

	Optional<Member> findByUniqueId(String uniqueId);

	Optional<Member> findByEmail(String email);

}
