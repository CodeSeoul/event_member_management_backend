/* (C) 2023 */
package org.codeseoul.event_member_management.series;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;
import org.codeseoul.event_member_management.event.Event;

@Entity
@NoArgsConstructor
@Data
public class Series extends Auditable {
  private @Id @GeneratedValue Long id;
  private String name;

  @OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "event-series")
  private List<Event> events = new ArrayList<>();

  public Series(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Series series)) return false;
    return Objects.equals(this.id, series.id) && Objects.equals(this.name, series.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name);
  }

  @Override
  public String toString() {
    return "Series{" + "id=" + this.id + ", title='" + this.name + "'}";
  }
}
