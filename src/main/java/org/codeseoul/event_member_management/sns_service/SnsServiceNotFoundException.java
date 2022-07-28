package org.codeseoul.event_member_management.sns_service;

public class SnsServiceNotFoundException extends RuntimeException {
    SnsServiceNotFoundException(Long id) {
        super("Could not find SNS service " + id);
    }
}
