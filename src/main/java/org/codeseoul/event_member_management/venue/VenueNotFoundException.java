/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.venue;

public class VenueNotFoundException extends RuntimeException {
  public VenueNotFoundException(Long id) {
    super("Could not find venue " + id);
  }
}
