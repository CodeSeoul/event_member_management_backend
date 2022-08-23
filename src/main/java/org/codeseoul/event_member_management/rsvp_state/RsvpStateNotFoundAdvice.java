package org.codeseoul.event_member_management.rsvp_state;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RsvpStateNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RsvpStateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rsvpStateNotFoundHandler(RsvpStateNotFoundException ex) {
        return ex.getMessage();
    }
}
