/* (C) 2023 */
package org.codeseoul.event_member_management.event;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import org.codeseoul.event_member_management.series.Series;
import org.codeseoul.event_member_management.series.SeriesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EventRestIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @Autowired private SeriesRepository seriesRepository;

  @Autowired private EventRepository eventRepository;

  private Series aSeries;
  private Event anEvent;

  @BeforeEach
  void setUp() {
    aSeries = new Series("New Series");
    seriesRepository.save(aSeries);
    anEvent =
        new Event(
            "New Event",
            "such new, so event",
            Timestamp.valueOf("2022-06-22 08:51:00"),
            120,
            "http://image.com",
            "WCoding",
            null,
            aSeries);
    eventRepository.save(anEvent);
  }

  @AfterEach
  void tearDown() {
    eventRepository.deleteAll();
    seriesRepository.deleteAll();
  }

  private Event createAnotherEvent() {
    return new Event(
        "Another Event",
        "This is madness",
        Timestamp.valueOf("2022-06-23 08:51:00"),
        120,
        "https://imgs.com",
        "Under da sea",
        null,
        aSeries);
  }

  @Test
  public void savesAnEvent() throws Exception {
    Event eventToBeSaved = createAnotherEvent();

    mockMvc
        .perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventToBeSaved)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(eventToBeSaved.getTitle())));
  }

  @Test
  void returnsABadRequestErrorWhenSavingAnInvalidEvent() throws Exception {
    Event anInvalidEvent = new Event();
    anInvalidEvent.setTitle("");
    mockMvc
        .perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInvalidEvent)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title", is("must not be blank")));
  }

  @Test
  public void findsAllEvents() throws Exception {
    Event anotherEvent = createAnotherEvent();
    eventRepository.save(anotherEvent);

    mockMvc
        .perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.events", hasSize(2)))
        .andExpect(jsonPath("$._embedded.events[0].title", is(anEvent.getTitle())))
        .andExpect(jsonPath("$._embedded.events[1].title", is(anotherEvent.getTitle())));
  }

  @Test
  void findsAnEventById() throws Exception {
    mockMvc
        .perform(get(String.format("/events/%d", anEvent.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(anEvent.getTitle())));
  }

  @Test
  public void updatesAnExistingEvent() throws Exception {
    anEvent.setTitle("Updated Event Title");

    mockMvc
        .perform(
            put(String.format("/events/%d", anEvent.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anEvent)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Updated Event Title")));
  }

  @Test
  public void returnsNotFoundErrorWhenUpdatingNonExistingEvent() throws Exception {
    Event notExistingEvent = createAnotherEvent();

    mockMvc
        .perform(
            put(String.format("/events/%d", anEvent.getId() + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notExistingEvent)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void returnsNotFoundErrorWhenEventNotFound() throws Exception {
    mockMvc
        .perform(get(String.format("/events/%d", anEvent.getId() + 2)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deletesAnEvent() throws Exception {
    mockMvc
        .perform(delete(String.format("/events/%d", anEvent.getId())))
        .andExpect(status().isOk());
  }

  @Test
  public void returnsNotFoundWhenDeletingNonExistingEvent() throws Exception {
    mockMvc
        .perform(delete(String.format("/events/%d", anEvent.getId() + 1)))
        .andExpect(status().isNotFound());
  }
}
