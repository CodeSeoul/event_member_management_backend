/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.venue;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class VenueNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(VenueNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String venueNotFoundHandler(VenueNotFoundException ex) {
    return ex.getMessage();
  }
}
