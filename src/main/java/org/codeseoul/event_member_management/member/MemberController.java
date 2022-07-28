package org.codeseoul.event_member_management.member;

import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.event.EventModelAssembler;
import org.codeseoul.event_member_management.event.EventNotFoundException;
import org.codeseoul.event_member_management.event.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberRepository repository;
    private final MemberModelAssembler assembler;

    MemberController(MemberRepository repository, MemberModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/members")
    CollectionModel<EntityModel<Member>> all() {

        List<EntityModel<Member>> members = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(members,
                linkTo(methodOn(MemberController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/members")
    EntityModel<Member> newMember(@RequestBody Member newMember) {
        return assembler.toModel(repository.save(newMember));
    }

    // Single item
    @GetMapping("/members/{id}")
    public EntityModel<Member> one(@PathVariable Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
        return assembler.toModel(member);
    }

    @PutMapping("/members/{id}")
    EntityModel<Member> replaceMember(@RequestBody Member member, @PathVariable Long id) {

        return repository.findById(id)
                .map(dbMember -> {
                    dbMember.setDisplayName(member.getDisplayName());
                    dbMember.setEmail(member.getEmail());
                    dbMember.setPhoneNumber(member.getPhoneNumber());
                    dbMember.setImageUrl(member.getImageUrl());
                    dbMember.setShortBio(member.getShortBio());
                    return assembler.toModel(repository.save(dbMember));
                })
                .orElseGet(() -> {
                    member.setId(id);
                    return assembler.toModel(repository.save(member));
                });
    }

    @DeleteMapping("/members/{id}")
    void deleteMember(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
