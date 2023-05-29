/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.member;

public class MemberNotFoundException extends RuntimeException {
  public MemberNotFoundException(Long id) {
    super("Could not find member " + id);
  }
}
