package com.devcci.devtoy.member.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m join fetch m.role where m.memberId = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);

    boolean existsByMemberId(String memberId);

    boolean existsByEmail(String email);
}