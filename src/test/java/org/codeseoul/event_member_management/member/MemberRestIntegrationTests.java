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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberRestIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberRepository memberRepository;

    Member aMember;

    @BeforeEach
    public void setUp() {
        aMember = new Member();
        aMember.setUsername("username");
        aMember.setEmail("example@email.com");
        memberRepository.saveAndFlush(aMember);
    }

    @AfterEach
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    public void findsAllMembers() throws Exception {
        Member anotherMember = new Member();
        anotherMember.setUsername("anotherMemberUsername");
        memberRepository.saveAndFlush(anotherMember);
        List<Member> members = new ArrayList<>();
        members.add(aMember);
        members.add(anotherMember);

        mockMvc.perform(get("/members"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.members", hasSize(2)))
            .andExpect(jsonPath("$._embedded.members[0].username", is(aMember.getUsername())))
            .andExpect(jsonPath("$._embedded.members[1].username", is(anotherMember.getUsername())));
    }

    @Test
    public void findsAMemberById() throws Exception {
        mockMvc.perform(get(String.format("/members/%d", aMember.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(aMember.getUsername())));
    }

    @Test
    public void returnsNotFoundErrorWhenMemberNotFound() throws Exception {
        mockMvc.perform(get("/members/2"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void savesAMember() throws Exception {
        Member memberToBeSaved = new Member();
        memberToBeSaved.setUsername("memberToBeSavedUsename");

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(memberToBeSaved)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(memberToBeSaved.getUsername())));
    }

    @Test
    public void returnsBadRequestErrorWhenSavingMemberWithBlankUsername() throws Exception {
        Member memberToBeSaved = new Member();
        memberToBeSaved.setUsername("");

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(memberToBeSaved)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.username", is("must not be blank")));
    }

    @Test
    public void returnsBadRequestrWhenSavingMemberwithInvalidEmail() throws Exception {
        Member memberToBeSaved = new Member();
        memberToBeSaved.setUsername("username");
        memberToBeSaved.setEmail("anInvalidEmail");

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(memberToBeSaved)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.email", is("must be a well-formed email address")));
    }

    @Test
    public void returnsBadRequestErrorWhenSavingMemberwithExistingUsername() throws Exception {
        Member memberToBeSaved = new Member();
        memberToBeSaved.setUsername(aMember.getUsername());

        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(memberToBeSaved)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.username", is("username already in use")));
    }

    @Test
    public void updatesAnExistingMember() throws Exception {
        aMember.setUsername("newUsername");
        mockMvc.perform(put(String.format("/members/%d", aMember.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aMember)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(aMember.getUsername())));
    }

    @Test
    public void returnsBadRequestErrorWhenUpdatingMemberwithExistingUsername() throws Exception {
        Member anotherMember = new Member();
        anotherMember.setUsername("anotherUsername");
        memberRepository.saveAndFlush(anotherMember);
        aMember.setUsername(anotherMember.getUsername());

        mockMvc.perform(put(String.format("/members/%d", aMember.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aMember)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.username", is("username already in use")));
    }

    @Test
    public void returnsNotFoundErrorWhenUpdatingNonExistingMember() throws Exception {
        Member anotherMember = new Member();
        anotherMember.setUsername("anotherMemberUsername");
        
        mockMvc.perform(put(String.format("/members/%d", aMember.getId()+1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(anotherMember)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deletesAMember() throws Exception {
        mockMvc.perform(delete(String.format("/members/%d", aMember.getId())))
            .andExpect(status().isOk());
    }  

    @Test
    public void returnsNotFoundErrorWhendeletingNonExistingMember() throws Exception {
        Long nonExistingMemberId = aMember.getId() + 1;
        mockMvc.perform(delete(String.format("/members/%d", nonExistingMemberId)))
            .andExpect(status().isNotFound());
    } 
}
