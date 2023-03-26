package org.codeseoul.event_member_management.member;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private MemberRepository memberRepository;

	Member aMember;

	@BeforeEach
	public void setUp() {
		aMember = new Member();
        aMember.setUsername("aMemberUsername");
	}

	@Test
	public void findsAllMembers() throws Exception {
        Member anotherMember = new Member();
        anotherMember.setUsername("anotherMemberUsername");
		List<Member> members = new ArrayList<>();
		members.add(aMember);
        members.add(anotherMember);
		when(memberRepository.findAll()).thenReturn(members);

		mockMvc.perform(get("/members"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.members", hasSize(2)))
			.andExpect(jsonPath("$._embedded.members[0].username", is(aMember.getUsername())))
            .andExpect(jsonPath("$._embedded.members[1].username", is(anotherMember.getUsername())));
	}

    @Test
	public void findsAMemberById() throws Exception {
        aMember.setId((long) 1);
		when(memberRepository.findById(aMember.getId())).thenReturn(Optional.of(aMember));

		mockMvc.perform(get("/members/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username", is(aMember.getUsername())));
	}

    @Test
	public void returnsNotFoundWhenMemberNotFound() throws Exception {
        aMember.setId((long) 1);
		when(memberRepository.findById(aMember.getId())).thenReturn(Optional.empty());

		mockMvc.perform(get("/members/1"))
			.andExpect(status().isNotFound());
	}

	@Test
	public void savesAMember() throws Exception {
		when(memberRepository.save(aMember)).thenReturn(aMember);

        mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username", is(aMember.getUsername())));
	}

    @Test
	public void updatesAMember() throws Exception {
        Member anUpdatedMember = new Member();
        anUpdatedMember.setId((long) 1);
        anUpdatedMember.setUsername("newMemberUsername");
        when(memberRepository.findById(aMember.getId())).thenReturn(Optional.of(aMember));
		when(memberRepository.save(anUpdatedMember)).thenReturn(anUpdatedMember);
		
        mockMvc.perform(put("/members/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anUpdatedMember)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username", is(anUpdatedMember.getUsername())));
	}

	@Test
	public void deletesAMember() throws Exception {
		aMember.setId((long) 1);

		mockMvc.perform(delete("/members/1"))
			.andExpect(status().isOk());
		verify(memberRepository).deleteById(aMember.getId());
	}  
}
