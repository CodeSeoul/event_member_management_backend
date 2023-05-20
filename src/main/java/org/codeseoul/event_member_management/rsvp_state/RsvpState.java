/* CodeSeoul (C) 2023 */
package org.codeseoul.event_member_management.rsvp_state;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class RsvpState {
  private @Id @GeneratedValue Long id;

  private String state;

  public RsvpState(String state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RsvpState rsvpState)) return false;
    return Objects.equals(this.id, rsvpState.id) && Objects.equals(this.state, rsvpState.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.state);
  }

  @Override
  public String toString() {
    return "RsvpState{" + "id=" + this.id + ", state='" + this.state + "'}";
  }
}
