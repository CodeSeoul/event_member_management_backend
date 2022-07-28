package org.codeseoul.event_member_management.rsvp;

import org.codeseoul.event_member_management.event.EventController;
import org.codeseoul.event_member_management.member.MemberController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RsvpModelAssembler implements RepresentationModelAssembler<Rsvp, EntityModel<Rsvp>> {

    @Override
    @NonNull
    public EntityModel<Rsvp> toModel(@NonNull Rsvp rsvp) {

        return EntityModel.of(rsvp,
                linkTo(methodOn(RsvpController.class).one(rsvp.getId())).withSelfRel(),
                linkTo(methodOn(MemberController.class).one(rsvp.getMember().getId())).withRel("member"),
                linkTo(methodOn(EventController.class).one(rsvp.getEvent().getId())).withRel("event"));
    }
}