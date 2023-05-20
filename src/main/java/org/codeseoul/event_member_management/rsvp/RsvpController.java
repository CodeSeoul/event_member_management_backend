/* (C) 2023 */
package org.codeseoul.event_member_management.rsvp;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventNotFoundException;
import org.codeseoul.event_member_management.event.EventRepository;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.member.MemberNotFoundException;
import org.codeseoul.event_member_management.member.MemberRepository;
import org.codeseoul.event_member_management.rsvp_state.RsvpState;
import org.codeseoul.event_member_management.rsvp_state.RsvpStateNotFoundException;
import org.codeseoul.event_member_management.rsvp_state.RsvpStateRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class RsvpController {

  private final RsvpRepository repository;
  private final RsvpModelAssembler assembler;
  private final EventRepository eventRepository;
  private final MemberRepository memberRepository;
  private final RsvpStateRepository rsvpStateRepository;

  RsvpController(
      RsvpRepository repository,
      RsvpModelAssembler assembler,
      EventRepository eventRepository,
      MemberRepository memberRepository,
      RsvpStateRepository rsvpStateRepository) {
    this.repository = repository;
    this.assembler = assembler;
    this.eventRepository = eventRepository;
    this.memberRepository = memberRepository;
    this.rsvpStateRepository = rsvpStateRepository;
  }

  @GetMapping("/members/{memberId}/rsvps")
  CollectionModel<EntityModel<RsvpReadSchema>> rsvpsForMember(@PathVariable Long memberId) {
    Optional<Member> member = memberRepository.findById(memberId);
    if (member.isEmpty()) {
      throw new MemberNotFoundException(memberId);
    }

    List<EntityModel<RsvpReadSchema>> rsvps =
        repository.findRsvpsByMemberId(memberId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(
        rsvps, linkTo(methodOn(RsvpController.class).rsvpsForMember(memberId)).withSelfRel());
  }

  @GetMapping("/event/{eventId}/rsvps")
  CollectionModel<EntityModel<RsvpReadSchema>> rsvpsForEvent(@PathVariable Long eventId) {
    Optional<Event> event = eventRepository.findById(eventId);
    if (event.isEmpty()) {
      throw new EventNotFoundException(eventId);
    }

    List<EntityModel<RsvpReadSchema>> rsvps =
        repository.findRsvpsByEventId(eventId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(
        rsvps, linkTo(methodOn(RsvpController.class).rsvpsForEvent(eventId)).withSelfRel());
  }

  @GetMapping("/rsvps/{id}")
  public EntityModel<RsvpReadSchema> one(@PathVariable Long id) {
    Rsvp rsvp = repository.findById(id).orElseThrow(() -> new RsvpNotFoundException(id));
    return assembler.toModel(rsvp);
  }

  // TODO: remove member part when auth is set up
  @PostMapping("/event/{eventId}/member/{memberId}/rsvp")
  EntityModel<RsvpReadSchema> newRsvp(
      @PathVariable Long eventId,
      @PathVariable Long memberId,
      @RequestBody RsvpStateSchema stateSchema) {
    Optional<Event> event = eventRepository.findById(eventId);
    if (event.isEmpty()) {
      throw new EventNotFoundException(eventId);
    }

    Optional<Member> member = memberRepository.findById(memberId);
    if (member.isEmpty()) {
      throw new MemberNotFoundException(memberId);
    }

    Optional<RsvpState> state = rsvpStateRepository.findById(stateSchema.getStateId());
    if (state.isEmpty()) {
      throw new RsvpStateNotFoundException(stateSchema.getStateId());
    }

    Rsvp rsvp = new Rsvp(member.get(), event.get(), state.get());
    return assembler.toModel(repository.save(rsvp));
  }

  @PutMapping("/rsvps/{id}")
  EntityModel<RsvpReadSchema> replaceMember(@RequestBody Rsvp rsvp, @PathVariable Long id) {

    return repository
        .findById(id)
        .map(
            dbRsvp -> {
              dbRsvp.setState(rsvp.getState());
              return assembler.toModel(repository.save(dbRsvp));
            })
        .orElseThrow(() -> new RsvpNotFoundException(id));
  }
}
