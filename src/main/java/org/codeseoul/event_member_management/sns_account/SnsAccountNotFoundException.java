package org.codeseoul.event_member_management.sns_account;

public class SnsAccountNotFoundException extends RuntimeException {
    public SnsAccountNotFoundException(Long id) {
        super("Could not find sns account " + id);
    }
}
