/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp_state;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RsvpStateController {

  private final RsvpStateRepository repository;
  private final RsvpStateModelAssembler assembler;

  RsvpStateController(RsvpStateRepository repository, RsvpStateModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/rsvpStates")
  CollectionModel<EntityModel<RsvpState>> all() {
    List<EntityModel<RsvpState>> rsvpStates =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(
        rsvpStates, linkTo(methodOn(RsvpStateController.class).all()).withRel("all"));
  }
}
