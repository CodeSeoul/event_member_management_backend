/* (C) 2023 */
package org.codeseoul.event_member_management.series;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class SeriesController {

  private final SeriesRepository repository;
  private final SeriesModelAssembler assembler;
  private final EventModelAssembler eventAssembler;

  SeriesController(
      SeriesRepository repository,
      SeriesModelAssembler assembler,
      EventModelAssembler eventAssembler) {
    this.repository = repository;
    this.assembler = assembler;
    this.eventAssembler = eventAssembler;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/series")
  CollectionModel<EntityModel<Series>> all() {

    List<EntityModel<Series>> seriesList =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(
        seriesList, linkTo(methodOn(SeriesController.class).all()).withRel("series"));
  }
  // end::get-aggregate-root[]

  @PostMapping("/series")
  EntityModel<Series> newSeries(@RequestBody Series newSeries) {
    return assembler.toModel(repository.save(newSeries));
  }

  // Single item
  @GetMapping("/series/{id}")
  public EntityModel<Series> one(@PathVariable Long id) {
    Series series = repository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));
    return assembler.toModel(series);
  }

  @PutMapping("/series/{id}")
  EntityModel<Series> replaceSeries(@RequestBody Series newSeries, @PathVariable Long id) {

    return repository
        .findById(id)
        .map(
            series -> {
              series.setName(newSeries.getName());
              return assembler.toModel(repository.save(series));
            })
        .orElseGet(
            () -> {
              newSeries.setId(id);
              return assembler.toModel(repository.save(newSeries));
            });
  }

  @DeleteMapping("/series/{id}")
  void deleteSeries(@PathVariable Long id) {
    repository.deleteById(id);
  }

  @GetMapping("/series/{id}/events")
  public CollectionModel<EntityModel<Event>> getEventsForSeries(@PathVariable Long id) {
    Series series = repository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));
    List<EntityModel<Event>> eventList =
        series.getEvents().stream().map(eventAssembler::toModel).collect(Collectors.toList());
    return CollectionModel.of(eventList);
  }
}
