package org.codeseoul.event_member_management.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByUsername(String username);
}
