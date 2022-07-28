package org.codeseoul.event_member_management.event;

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
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

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

        List<EntityModel<Event>> events = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(events,
                linkTo(methodOn(EventController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/events")
    EntityModel<Event> newEvent(@RequestBody Event newEvent) {
        return assembler.toModel(repository.save(newEvent));
    }

    // Single item
    @GetMapping("/events/{id}")
    public EntityModel<Event> one(@PathVariable Long id) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
        return assembler.toModel(event);
    }

    @PutMapping("/events/{id}")
    EntityModel<Event> replaceEvent(@RequestBody Event newEvent, @PathVariable Long id) {

        return repository.findById(id)
                .map(Event -> {
                    Event.setTitle(newEvent.getTitle());
                    Event.setDescription(newEvent.getDescription());
                    Event.setStartTimestamp(newEvent.getStartTimestamp());
                    Event.setDurationMinutes(newEvent.getDurationMinutes());
                    Event.setImageUrl(newEvent.getImageUrl());
                    Event.setVenue(newEvent.getVenue());
                    Event.setOnlineLink(newEvent.getOnlineLink());
                    Event.setSeries(newEvent.getSeries());
                    return assembler.toModel(repository.save(Event));
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return assembler.toModel(repository.save(newEvent));
                });
    }

    @DeleteMapping("/events/{id}")
    void deleteEvent(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
