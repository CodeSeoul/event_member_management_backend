package org.codeseoul.event_member_management.series;

public class SeriesNotFoundException extends RuntimeException {
    SeriesNotFoundException(Long id) {
        super("Could not find series " + id);
    }
}
