/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.member;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;
import org.codeseoul.event_member_management.rsvp.Rsvp;
import org.codeseoul.event_member_management.sns_account.SnsAccount;

@Entity
@NoArgsConstructor
@Data
public class Member extends Auditable {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private @Id @GeneratedValue Long id;

  @NotBlank
  @Column(unique = true)
  private String username;

  private String displayName;

  @Email
  @NotBlank
  @Column(unique = true)
  private String email;

  @NotBlank
  @Column(unique = true)
  private String phoneNumber;

  private String imageUrl;
  private String shortBio;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "rsvp-member")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Set<Rsvp> rsvps = new HashSet<>();

  @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private List<SnsAccount> snsAccounts = new ArrayList<>();

  public Member(
      String username,
      String displayName,
      String email,
      String phoneNumber,
      String imageUrl,
      String shortBio) {
    this.username = username;
    this.displayName = displayName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.imageUrl = imageUrl;
    this.shortBio = shortBio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Member member)) return false;
    return Objects.equals(this.id, member.id)
        && Objects.equals(this.username, member.username)
        && Objects.equals(this.email, member.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.username, this.email);
  }

  @Override
  public String toString() {
    return "Member{" + "id=" + this.id + ", username='" + this.username + "'}";
  }
}
