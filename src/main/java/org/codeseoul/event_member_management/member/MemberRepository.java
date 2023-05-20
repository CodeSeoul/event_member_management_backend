/* (C) 2023 */
package org.codeseoul.event_member_management.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  public List<Member> findByUsernameOrEmailOrPhoneNumber(
      String username, String email, String phoneNumber);
}
