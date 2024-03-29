/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp_state;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RsvpStateModelAssembler
    implements RepresentationModelAssembler<RsvpState, EntityModel<RsvpState>> {

  @Override
  @NonNull public EntityModel<RsvpState> toModel(@NonNull RsvpState rsvpState) {

    return EntityModel.of(
        rsvpState, linkTo(methodOn(RsvpStateController.class).all()).withRel("all"));
  }
}
