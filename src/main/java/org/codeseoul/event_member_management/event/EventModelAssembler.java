package org.codeseoul.event_member_management.event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.codeseoul.event_member_management.series.Series;
import org.codeseoul.event_member_management.series.SeriesController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class EventModelAssembler implements RepresentationModelAssembler<Event, EntityModel<Event>> {

    @Override
    @NonNull
    public EntityModel<Event> toModel(@NonNull Event event) {

        EntityModel<Event> eventModel = EntityModel.of(event,
                linkTo(methodOn(EventController.class).one(event.getId())).withSelfRel(),
                linkTo(methodOn(EventController.class).all()).withRel("events"));

        Series series = event.getSeries();
        if (series != null) {
            eventModel.add(linkTo(methodOn(SeriesController.class).one(series.getId())).withRel("series"));
            eventModel.add(linkTo(methodOn(SeriesController.class).getEventsForSeries(series.getId())).withRel("allEventsForSeries"));
        }
        return eventModel;
    }
}