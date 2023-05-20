/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.venue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class VenueModelAssembler
    implements RepresentationModelAssembler<Venue, EntityModel<Venue>> {

  @Override
  @NonNull public EntityModel<Venue> toModel(@NonNull Venue venue) {

    return EntityModel.of(
        venue, linkTo(methodOn(VenueController.class).one(venue.getId())).withSelfRel());
  }
}
