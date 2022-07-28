package org.codeseoul.event_member_management.rsvp;

import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventNotFoundException;
import org.codeseoul.event_member_management.event.EventRepository;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.member.MemberNotFoundException;
import org.codeseoul.event_member_management.member.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RsvpController {

    private static final Logger log = LoggerFactory.getLogger(RsvpController.class);

    private final RsvpRepository repository;
    private final RsvpModelAssembler assembler;
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    RsvpController(RsvpRepository repository, RsvpModelAssembler assembler, EventRepository eventRepository, MemberRepository memberRepository) {
        this.repository = repository;
        this.assembler = assembler;
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members/{memberId}/rsvps")
    CollectionModel<EntityModel<Rsvp>> rsvpsForMember(@PathVariable Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new MemberNotFoundException(memberId);
        }

        List<EntityModel<Rsvp>> rsvps = repository.findRsvpsByMemberId(memberId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rsvps,
                linkTo(methodOn(RsvpController.class).rsvpsForMember(memberId)).withSelfRel());
    }

    @GetMapping("/event/{eventId}/rsvps")
    CollectionModel<EntityModel<Rsvp>> rsvpsForEvent(@PathVariable Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EventNotFoundException(eventId);
        }

        List<EntityModel<Rsvp>> rsvps = repository.findRsvpsByEventId(eventId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(rsvps,
                linkTo(methodOn(RsvpController.class).rsvpsForEvent(eventId)).withSelfRel());
    }

    @GetMapping("/rsvps/{id}")
    public EntityModel<Rsvp> one(@PathVariable Long id) {
        Rsvp rsvp = repository.findById(id)
                .orElseThrow(() -> new RsvpNotFoundException(id));
        return assembler.toModel(rsvp);
    }

    // TODO: remove member part when auth is set up
    @PostMapping("/event/{eventId}/member/{memberId}/rsvp")
    EntityModel<Rsvp> newRsvp(@RequestBody Rsvp newRsvp) {
        return assembler.toModel(repository.save(newRsvp));
    }

    @PutMapping("/rsvps/{id}")
    EntityModel<Rsvp> replaceMember(@RequestBody Rsvp rsvp, @PathVariable Long id) {

        return repository.findById(id)
                .map(dbRsvp -> {
                    dbRsvp.setState(rsvp.getState());
                    return assembler.toModel(repository.save(dbRsvp));
                })
                .orElseGet(() -> {
                    rsvp.setId(id);
                    return assembler.toModel(repository.save(rsvp));
                });
    }
}
