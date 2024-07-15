package com.cotato.squadus.domain.auth.repository;
import com.cotato.squadus.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByUniqueId(String uniqueId);

    Optional<Member> findByEmail(String email);

}
