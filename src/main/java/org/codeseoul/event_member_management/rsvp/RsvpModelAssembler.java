/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.codeseoul.event_member_management.event.EventController;
import org.codeseoul.event_member_management.member.MemberController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RsvpModelAssembler
    implements RepresentationModelAssembler<Rsvp, EntityModel<RsvpReadSchema>> {

  @Override
  @NonNull public EntityModel<RsvpReadSchema> toModel(@NonNull Rsvp rsvp) {

    RsvpReadSchema rsvpReadSchema = new RsvpReadSchema(rsvp);
    return EntityModel.of(
        rsvpReadSchema,
        linkTo(methodOn(RsvpController.class).one(rsvp.getId())).withSelfRel(),
        linkTo(methodOn(MemberController.class).one(rsvp.getMember().getId())).withRel("member"),
        linkTo(methodOn(EventController.class).one(rsvp.getEvent().getId())).withRel("event"));
  }
}
