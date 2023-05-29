/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
  List<Rsvp> findRsvpsByMemberId(Long memberId);

  List<Rsvp> findRsvpsByEventId(Long eventId);
}
