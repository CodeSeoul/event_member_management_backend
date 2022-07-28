package org.codeseoul.event_member_management.sns_account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SnsAccountNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(SnsAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String snsAccountNotFoundHandler(SnsAccountNotFoundException ex) {
        return ex.getMessage();
    }
}
