/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.venue;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class VenueRestIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @Autowired private VenueRepository venueRepository;

  private Venue aVenue;

  @BeforeEach
  public void setUp() {
    aVenue =
        new Venue(
            "WCoding",
            "서울 영등포구 선유로49길 23 11층 1101호",
            "https://naver.me/FiLsgFXq",
            "",
            "https://place.map.kakao.com/1096784914",
            "https://g.page/Wcoding?share");
    venueRepository.saveAndFlush(aVenue);
  }

  @AfterEach
  public void tearDown() {
    venueRepository.deleteAll();
  }

  private Venue createAnotherVenue() {
    return new Venue(
        "Lunit Office",
        "서울 강남구 강남대로 374 4층",
        "https://naver.me/5eTcsvRc",
        "",
        "",
        "https://goo.gl/maps/bJngfafSgjdEEadN9");
  }

  @Test
  public void findsAllVenues() throws Exception {
    Venue anotherVenue = createAnotherVenue();
    venueRepository.saveAndFlush(anotherVenue);

    mockMvc
        .perform(get("/venues"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.venues", hasSize(2)))
        .andExpect(jsonPath("$._embedded.venues[0].name", is(aVenue.getName())))
        .andExpect(jsonPath("$._embedded.venues[1].name", is(anotherVenue.getName())));
  }

  @Test
  public void savesAVenue() throws Exception {
    Venue venueToBeSaved = createAnotherVenue();

    mockMvc
        .perform(
            post("/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(venueToBeSaved)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(venueToBeSaved.getName())))
        .andExpect(jsonPath("$.address", is(venueToBeSaved.getAddress())));
  }

  @Test
  public void findsAVenueById() throws Exception {
    mockMvc
        .perform(get(String.format("/venues/%d", aVenue.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(aVenue.getName())));
  }

  @Test
  public void returnsNotFoundErrorWhenVenueNotFound() throws Exception {
    mockMvc
        .perform(get(String.format("/venues/%d", aVenue.getId() + 2)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updatesAnExistingVenue() throws Exception {
    aVenue.setName("Updated Venue Name");

    mockMvc
        .perform(
            put(String.format("/venues/%d", aVenue.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aVenue)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Updated Venue Name")));
  }

  @Test
  public void returnsNotFoundErrorWhenUpdatingNotExisitingVenue() throws Exception {
    Venue nonExistingVenue = createAnotherVenue();

    mockMvc
        .perform(
            put(String.format("/venues/%d", aVenue.getId() + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(nonExistingVenue)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deletesAVenue() throws Exception {
    mockMvc.perform(delete(String.format("/venues/%d", aVenue.getId()))).andExpect(status().isOk());
  }

  @Test
  public void returnsNotFoundWhenDeletingNonExistingVenue() throws Exception {
    mockMvc
        .perform(delete(String.format("/venues/%d", aVenue.getId() + 1)))
        .andExpect(status().isNotFound());
  }
}
