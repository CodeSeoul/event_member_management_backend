/* (C) 2023 */
package org.codeseoul.event_member_management.rsvp;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventRepository;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.member.MemberRepository;
import org.codeseoul.event_member_management.rsvp_state.RsvpState;
import org.codeseoul.event_member_management.rsvp_state.RsvpStateRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RsvpRestIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private RsvpRepository rsvpRepository;

  @Autowired private EventRepository eventRepository;
  @Autowired private MemberRepository memberRepository;

  @Autowired private RsvpStateRepository rsvpStateRepository;

  @Autowired private ObjectMapper mapper;

  private Event anEvent;
  private Member aMember;

  private RsvpState rsvpYesState;
  private RsvpState rsvpNoState;

  private Rsvp anRsvp;

  @BeforeEach
  void setUp() {
    rsvpNoState = rsvpStateRepository.save(new RsvpState("no"));
    rsvpYesState = rsvpStateRepository.save(new RsvpState("yes"));
    aMember =
        memberRepository.saveAndFlush(
            new Member(
                "user",
                "displayName",
                "example@email.com",
                "01234567890",
                "http://image.com",
                "About user"));
    anEvent =
        eventRepository.save(
            new Event(
                "New Event",
                "such new, so event",
                Timestamp.valueOf("2022-06-22 08:51:00"),
                120,
                "http://image.com",
                "WCoding",
                null,
                null));
    anRsvp = new Rsvp(aMember, anEvent, rsvpYesState);
    rsvpRepository.save(anRsvp);
  }

  @AfterEach
  void tearDown() {
    rsvpRepository.deleteAll();
    rsvpStateRepository.deleteAll();
    eventRepository.deleteAll();
    memberRepository.deleteAll();
  }

  private Member createAnotherMember() {
    return new Member(
        "anotherUser",
        "Another Display Name",
        "anotherExample@email.com",
        "2345678901",
        "http://image.com",
        "About another user");
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
        null);
  }

  @Test
  void returnsNotFoundErrorWhenRsvpNotFound() throws Exception {
    mockMvc
        .perform(get(String.format("/rsvps/%d", anRsvp.getId() + 1)))
        .andExpect(status().isNotFound());
  }

  @Test
  void findsRspvById() throws Exception {
    mockMvc
        .perform(get(String.format("/rsvps/%d", anRsvp.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eventTitle", is(anEvent.getTitle())))
        .andExpect(jsonPath("$.memberDisplayName", is(aMember.getDisplayName())));
  }

  @Test
  void returnsNotFoundErrorWhenUpdatingNonExistingRsvp() throws Exception {
    mockMvc
        .perform(
            put(String.format("/rsvps/%d", anRsvp.getId() + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anRsvp)))
        .andExpect(status().isNotFound());
  }

  @Test
  void updatesAnRsvp() throws Exception {
    anRsvp.setState(rsvpNoState);

    mockMvc
        .perform(
            put(String.format("/rsvps/%d", anRsvp.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anRsvp)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.rsvpState", is(rsvpNoState.getState())));
  }

  @Test
  void getsAllRsvpsForEvent() throws Exception {
    Member anotherMember = createAnotherMember();
    memberRepository.save(anotherMember);
    Rsvp anotherRsvp = new Rsvp(anotherMember, anEvent, rsvpNoState);
    rsvpRepository.save(anotherRsvp);

    mockMvc
        .perform(get(String.format("/event/%d/rsvps", anEvent.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.rsvpReadSchemas", hasSize(2)))
        .andExpect(
            jsonPath("$._embedded.rsvpReadSchemas[0].rsvpState", is(rsvpYesState.getState())))
        .andExpect(
            jsonPath(
                "$._embedded.rsvpReadSchemas[0].memberDisplayName", is(aMember.getDisplayName())))
        .andExpect(jsonPath("$._embedded.rsvpReadSchemas[1].rsvpState", is(rsvpNoState.getState())))
        .andExpect(
            jsonPath(
                "$._embedded.rsvpReadSchemas[1].memberDisplayName",
                is(anotherMember.getDisplayName())));
  }

  @Test
  void getsAllMembersRsvps() throws Exception {
    Event anotherEvent = createAnotherEvent();
    eventRepository.save(anotherEvent);
    Rsvp anotherRsvp = new Rsvp(aMember, anotherEvent, rsvpNoState);
    rsvpRepository.save(anotherRsvp);

    mockMvc
        .perform(get(String.format("/members/%d/rsvps", aMember.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.rsvpReadSchemas", hasSize(2)))
        .andExpect(
            jsonPath("$._embedded.rsvpReadSchemas[0].rsvpState", is(rsvpYesState.getState())))
        .andExpect(jsonPath("$._embedded.rsvpReadSchemas[0].eventTitle", is(anEvent.getTitle())))
        .andExpect(
            jsonPath(
                "$._embedded.rsvpReadSchemas[0].memberDisplayName", is(aMember.getDisplayName())))
        .andExpect(jsonPath("$._embedded.rsvpReadSchemas[1].rsvpState", is(rsvpNoState.getState())))
        .andExpect(
            jsonPath(
                "$._embedded.rsvpReadSchemas[1].memberDisplayName", is(aMember.getDisplayName())))
        .andExpect(
            jsonPath("$._embedded.rsvpReadSchemas[1].eventTitle", is(anotherEvent.getTitle())));
  }

  @Test
  void createsANewRsvp() throws Exception {
    Member anotherMember = createAnotherMember();
    memberRepository.save(anotherMember);
    RsvpStateSchema stateSchema = new RsvpStateSchema();
    stateSchema.setStateId(rsvpNoState.getId());

    mockMvc
        .perform(
            post(String.format("/event/%d/member/%d/rsvp", anEvent.getId(), anotherMember.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(stateSchema)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberDisplayName", is(anotherMember.getDisplayName())))
        .andExpect(jsonPath("$.eventTitle", is(anEvent.getTitle())))
        .andExpect(jsonPath("$.rsvpState", is(rsvpNoState.getState())));
  }
}
