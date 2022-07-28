package org.codeseoul.event_member_management.series;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class SeriesModelAssembler implements RepresentationModelAssembler<Series, EntityModel<Series>> {

    @Override
    @NonNull
    public EntityModel<Series> toModel(@NonNull Series series) {

        return EntityModel.of(series,
                linkTo(methodOn(SeriesController.class).one(series.getId())).withSelfRel(),
                linkTo(methodOn(SeriesController.class).getEventsForSeries(series.getId())).withRel("events"));
    }
}