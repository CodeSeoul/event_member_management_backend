package org.codeseoul.event_member_management.rsvp;

public class RsvpNotFoundException extends RuntimeException {
    public RsvpNotFoundException(Long id) {
        super("Could not find rsvp " + id);
    }
}
