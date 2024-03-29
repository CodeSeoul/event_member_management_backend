/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SnsServiceNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(SnsServiceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String snsServiceNotFoundHandler(SnsServiceNotFoundException ex) {
    return ex.getMessage();
  }
}
