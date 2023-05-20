/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.venue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class VenueController {

  private final VenueRepository repository;
  private final VenueModelAssembler assembler;

  VenueController(VenueRepository repository, VenueModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/venues")
  CollectionModel<EntityModel<Venue>> all() {

    List<EntityModel<Venue>> venues =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(venues, linkTo(methodOn(VenueController.class).all()).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/venues")
  EntityModel<Venue> newVenue(@RequestBody Venue newVenue) {
    return assembler.toModel(repository.save(newVenue));
  }

  // Single item
  @GetMapping("/venues/{id}")
  public EntityModel<Venue> one(@PathVariable Long id) {
    Venue venue = repository.findById(id).orElseThrow(() -> new VenueNotFoundException(id));
    return assembler.toModel(venue);
  }

  @PutMapping("/venues/{id}")
  EntityModel<Venue> replaceVenue(@RequestBody Venue newVenue, @PathVariable Long id) {
    if (!repository.existsById(id)) throw new VenueNotFoundException(id);
    newVenue.setId(id);
    return assembler.toModel(repository.save(newVenue));
  }
  ;

  @DeleteMapping("/venues/{id}")
  void deleteVenue(@PathVariable Long id) {
    if (!repository.existsById(id)) throw new VenueNotFoundException(id);
    repository.deleteById(id);
  }
}
