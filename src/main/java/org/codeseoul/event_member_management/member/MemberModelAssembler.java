package org.codeseoul.event_member_management.member;

import org.codeseoul.event_member_management.sns_account.SnsAccountController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MemberModelAssembler implements RepresentationModelAssembler<Member, EntityModel<Member>> {

    @Override
    @NonNull
    public EntityModel<Member> toModel(@NonNull Member member) {

        return EntityModel.of(member,
                linkTo(methodOn(MemberController.class).one(member.getId())).withSelfRel(),
                linkTo(methodOn(MemberController.class).all()).withRel("members"),
                linkTo(methodOn(SnsAccountController.class).allForMember(member.getId())).withRel("snsAccounts"));
    }
}