/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp_state;

public class RsvpStateNotFoundException extends RuntimeException {
  public RsvpStateNotFoundException(Long id) {
    super("Could not find rsvp state" + id);
  }
}
