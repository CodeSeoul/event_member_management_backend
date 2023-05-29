/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class SnsServiceController {

  private final SnsServiceRepository repository;
  private final SnsServiceModelAssembler assembler;

  SnsServiceController(SnsServiceRepository repository, SnsServiceModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/snsServices")
  CollectionModel<EntityModel<SnsService>> all() {

    List<EntityModel<SnsService>> events =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(
        events, linkTo(methodOn(SnsServiceController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  // Single item
  @GetMapping("/snsServices/{id}")
  public EntityModel<SnsService> one(@PathVariable Long id) {
    SnsService event =
        repository.findById(id).orElseThrow(() -> new SnsServiceNotFoundException(id));
    return assembler.toModel(event);
  }
}
