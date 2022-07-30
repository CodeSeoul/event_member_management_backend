package org.codeseoul.event_member_management.venue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class VenueController {

    private static final Logger log = LoggerFactory.getLogger(VenueController.class);

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

        List<EntityModel<Venue>> venues = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(venues,
                linkTo(methodOn(VenueController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/venues")
    EntityModel<Venue> newVenue(@RequestBody Venue newVenue) {
        return assembler.toModel(repository.save(newVenue));
    }

    // Single item
    @GetMapping("/venues/{id}")
    public EntityModel<Venue> one(@PathVariable Long id) {
        Venue venue = repository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException(id));
        return assembler.toModel(venue);
    }

    @PutMapping("/venues/{id}")
    EntityModel<Venue> replaceVenue(@RequestBody Venue newVenue, @PathVariable Long id) {

        return repository.findById(id)
                .map(Venue -> {
                    Venue.setName(newVenue.getName());
                    Venue.setAddress(newVenue.getAddress());
                    Venue.setNaverMapsLink(newVenue.getNaverMapsLink());
                    Venue.setTmapLink(newVenue.getTmapLink());
                    Venue.setKakaoMapsLink(newVenue.getKakaoMapsLink());
                    Venue.setGoogleMapsLink(newVenue.getGoogleMapsLink());
                    return assembler.toModel(repository.save(Venue));
                })
                .orElseGet(() -> {
                    newVenue.setId(id);
                    return assembler.toModel(repository.save(newVenue));
                });
    }

    @DeleteMapping("/venues/{id}")
    void deleteVenue(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
