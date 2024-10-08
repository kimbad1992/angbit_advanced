package com.angbit.angbit_advanced.repository;

import com.angbit.angbit_advanced.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // AuthProvider에서 지연 초기화 문제로 Fetch Join
    @Query("SELECT m FROM Member m JOIN FETCH m.roles WHERE m.username = :username")
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
