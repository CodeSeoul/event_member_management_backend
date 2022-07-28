package org.codeseoul.event_member_management.rsvp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RsvpNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RsvpNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String rsvpNotFoundHandler(RsvpNotFoundException ex) {
        return ex.getMessage();
    }
}
