/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

  private final EventRepository repository;
  private final EventModelAssembler assembler;

  EventController(EventRepository repository, EventModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/events")
  CollectionModel<EntityModel<Event>> all() {

    List<EntityModel<Event>> events =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(events, linkTo(methodOn(EventController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/events")
  EntityModel<Event> newEvent(@Valid @RequestBody Event newEvent) {
    return assembler.toModel(repository.save(newEvent));
  }

  // Single item
  @GetMapping("/events/{id}")
  public EntityModel<Event> one(@PathVariable Long id) {
    Event event = repository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
    return assembler.toModel(event);
  }

  @PutMapping("/events/{id}")
  EntityModel<Event> replaceEvent(@Valid @RequestBody Event newEvent, @PathVariable Long id) {
    if (!repository.existsById(id)) throw new EventNotFoundException(id);
    newEvent.setId(id);
    return assembler.toModel(repository.save(newEvent));
  }

  @DeleteMapping("/events/{id}")
  void deleteEvent(@PathVariable Long id) {
    if (!repository.existsById(id)) throw new EventNotFoundException(id);
    repository.deleteById(id);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return errors;
  }
}
