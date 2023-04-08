package org.codeseoul.event_member_management.member;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<EntityModel<Member>> members = repository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(members,
            linkTo(methodOn(MemberController.class).all()).withSelfRel());
    }

    // end::get-aggregate-root[]
    @PostMapping("/members")
    EntityModel<Member> newMember(@Valid @RequestBody Member newMember, BindingResult errors) throws MethodArgumentNotValidException {
        validateMember(newMember, errors);
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
    EntityModel<Member> replaceMember(@Valid @RequestBody Member member, @PathVariable Long id, BindingResult errors) throws MethodArgumentNotValidException {
        validateMember(member, errors);
        return repository.findById(id)
            .map(dbMember -> {
                dbMember.setUsername(member.getUsername());
                dbMember.setDisplayName(member.getDisplayName());
                dbMember.setEmail(member.getEmail());
                dbMember.setPhoneNumber(member.getPhoneNumber());
                dbMember.setImageUrl(member.getImageUrl());
                dbMember.setShortBio(member.getShortBio());
                return assembler.toModel(repository.save(dbMember));
            }).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @DeleteMapping("/members/{id}")
    void deleteMember(@PathVariable Long id) {
        repository.findById(id)
            .orElseThrow(() -> new MemberNotFoundException(id));
        repository.deleteById(id);
    }

    private void validateMember(Member member, BindingResult errors) throws MethodArgumentNotValidException {
        if (repository.findByUsername(member.getUsername()).isPresent())
            errors.addError(new FieldError("Member", "username", "username already in use"));
        if (errors.hasErrors())
            throw new MethodArgumentNotValidException(null, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
