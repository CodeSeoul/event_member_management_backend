/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.codeseoul.event_member_management.member.MemberController;
import org.codeseoul.event_member_management.sns_service.SnsService;
import org.codeseoul.event_member_management.sns_service.SnsServiceController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SnsAccountModelAssembler
    implements RepresentationModelAssembler<SnsAccount, EntityModel<SnsAccount>> {

  @Override
  @NonNull public EntityModel<SnsAccount> toModel(@NonNull SnsAccount snsAccount) {
    SnsService snsService = snsAccount.getSnsService();
    return EntityModel.of(
        snsAccount,
        linkTo(methodOn(SnsAccountController.class).allForMember(snsAccount.getMember().getId()))
            .withRel("snsAccounts"),
        linkTo(methodOn(MemberController.class).one(snsAccount.getMember().getId()))
            .withRel("owningMember"),
        linkTo(methodOn(SnsServiceController.class).one(snsService.getId())).withRel("snsService"));
  }
}
