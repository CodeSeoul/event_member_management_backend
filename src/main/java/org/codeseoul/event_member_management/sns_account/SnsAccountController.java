/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.sns_account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.member.MemberNotFoundException;
import org.codeseoul.event_member_management.member.MemberRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class SnsAccountController {

  private final SnsAccountRepository repository;
  private final SnsAccountModelAssembler assembler;
  private final MemberRepository memberRepository;

  SnsAccountController(
      SnsAccountRepository repository,
      SnsAccountModelAssembler assembler,
      MemberRepository memberRepository) {
    this.repository = repository;
    this.assembler = assembler;
    this.memberRepository = memberRepository;
  }

  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/members/{memberId}/snsAccounts")
  public CollectionModel<EntityModel<SnsAccount>> allForMember(@PathVariable Long memberId) {

    List<EntityModel<SnsAccount>> snsAccounts =
        repository.findSnsAccountsByMemberId(memberId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(
        snsAccounts,
        linkTo(methodOn(SnsAccountController.class).allForMember(memberId)).withSelfRel());
  }
  // end::get-aggregate-root[]

  @PostMapping("/members/{memberId}/snsAccounts")
  EntityModel<SnsAccount> newSnsAccount(
      @PathVariable Long memberId, @RequestBody SnsAccount newSnsAccount) {
    Optional<Member> member = memberRepository.findById(memberId);
    if (member.isEmpty()) {
      throw new MemberNotFoundException(memberId);
    }
    newSnsAccount.setMember(member.get());
    return assembler.toModel(repository.save(newSnsAccount));
  }

  @DeleteMapping("/snsAccounts/{id}")
  void deleteSnsAccount(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
