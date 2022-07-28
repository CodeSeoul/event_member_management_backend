package org.codeseoul.event_member_management.rsvp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RsvpRepository extends JpaRepository<Rsvp, Long> {
    List<Rsvp> findRsvpsByMemberId(Long memberId);
    List<Rsvp> findRsvpsByEventId(Long eventId);
}
