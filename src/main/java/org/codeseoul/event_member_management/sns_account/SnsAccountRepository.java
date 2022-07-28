package org.codeseoul.event_member_management.sns_account;

import org.codeseoul.event_member_management.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnsAccountRepository extends JpaRepository<SnsAccount, Long> {
    List<SnsAccount> findSnsAccountByMember(Member member);
    List<SnsAccount> findSnsAccountsByMemberId(Long memberId);
}
