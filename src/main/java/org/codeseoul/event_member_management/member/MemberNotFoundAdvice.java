/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MemberNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(MemberNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String eventNotFoundHandler(MemberNotFoundException ex) {
    return ex.getMessage();
  }
}
