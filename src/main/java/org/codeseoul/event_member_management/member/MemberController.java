/* (C) 2023 */
package org.codeseoul.event_member_management.member;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

  // private static final Logger log = LoggerFactory.getLogger(MemberController.class);

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
    List<EntityModel<Member>> members =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
    return CollectionModel.of(
        members, linkTo(methodOn(MemberController.class).all()).withSelfRel());
  }

  // end::get-aggregate-root[]
  @PostMapping("/members")
  EntityModel<Member> newMember(@Valid @RequestBody Member newMember, BindingResult errors)
      throws MethodArgumentNotValidException {
    validateMember(newMember, errors);
    return assembler.toModel(repository.save(newMember));
  }

  // Single item
  @GetMapping("/members/{id}")
  public EntityModel<Member> one(@PathVariable Long id) {
    Member member = repository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    return assembler.toModel(member);
  }

  @PutMapping("/members/{id}")
  EntityModel<Member> replaceMember(
      @Valid @RequestBody Member newMember, @PathVariable Long id, BindingResult errors)
      throws MethodArgumentNotValidException {
    if (!repository.existsById(id)) throw new MemberNotFoundException(id);
    newMember.setId(id); //
    validateMember(newMember, errors);
    return assembler.toModel(repository.save(newMember));
  }

  @DeleteMapping("/members/{id}")
  void deleteMember(@PathVariable Long id) {
    repository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    repository.deleteById(id);
  }

  private void validateMember(Member member, BindingResult errors)
      throws MethodArgumentNotValidException {
    List<Member> members =
        repository.findByUsernameOrEmailOrPhoneNumber(
            member.getUsername(), member.getEmail(), member.getPhoneNumber());
    for (Member otherMember : members) {
      if (member.getId() != null && member.getId().equals(otherMember.getId())) continue;
      if (member.getUsername().equals(otherMember.getUsername()))
        errors.addError(new FieldError("Member", "username", "username already in use"));
      ;
      if (member.getEmail().equals(otherMember.getEmail()))
        errors.addError(new FieldError("Member", "email", "email already in use"));
      if (member.getPhoneNumber().equals(otherMember.getPhoneNumber()))
        errors.addError(new FieldError("Member", "phoneNumber", "phoneNumber already in use"));
    }
    if (errors.hasErrors()) throw new MethodArgumentNotValidException(null, errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return errors;
  }
}
