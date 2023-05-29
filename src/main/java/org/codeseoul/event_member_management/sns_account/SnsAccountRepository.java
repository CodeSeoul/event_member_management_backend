/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_account;

import java.util.List;
import org.codeseoul.event_member_management.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsAccountRepository extends JpaRepository<SnsAccount, Long> {
  List<SnsAccount> findSnsAccountByMember(Member member);

  List<SnsAccount> findSnsAccountsByMemberId(Long memberId);
}
