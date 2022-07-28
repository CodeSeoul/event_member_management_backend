package org.codeseoul.event_member_management.sns_service;

import org.codeseoul.event_member_management.event.EventController;
import org.codeseoul.event_member_management.series.SeriesController;
import org.codeseoul.event_member_management.sns_account.SnsAccount;
import org.codeseoul.event_member_management.sns_account.SnsAccountController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SnsServiceModelAssembler implements RepresentationModelAssembler<SnsService, EntityModel<SnsService>> {

    @Override
    @NonNull
    public EntityModel<SnsService> toModel(@NonNull SnsService snsService) {
        return EntityModel.of(snsService,
                linkTo(methodOn(SnsServiceController.class).one(snsService.getId())).withSelfRel(),
                linkTo(methodOn(SnsServiceController.class).all()).withRel("snsServices"));
    }
}