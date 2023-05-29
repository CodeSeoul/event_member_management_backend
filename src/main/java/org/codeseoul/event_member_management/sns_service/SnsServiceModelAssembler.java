/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SnsServiceModelAssembler
    implements RepresentationModelAssembler<SnsService, EntityModel<SnsService>> {

  @Override
  @NonNull public EntityModel<SnsService> toModel(@NonNull SnsService snsService) {
    return EntityModel.of(
        snsService,
        linkTo(methodOn(SnsServiceController.class).one(snsService.getId())).withSelfRel(),
        linkTo(methodOn(SnsServiceController.class).all()).withRel("snsServices"));
  }
}
