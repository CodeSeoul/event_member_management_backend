package org.codeseoul.event_member_management.event;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Could not find event " + id);
    }
}
