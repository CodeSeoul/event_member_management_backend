/* (C) 2023 */
package org.codeseoul.event_member_management.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;
import org.codeseoul.event_member_management.rsvp.Rsvp;
import org.codeseoul.event_member_management.series.Series;

@Entity
@NoArgsConstructor
@Data
public class Event extends Auditable {
  private @Id @GeneratedValue Long id;

  @NotBlank private String title;
  private String description;
  private Timestamp startTimestamp;
  private Integer durationMinutes;
  private String imageUrl;
  private String venue;
  private String onlineLink;

  @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "rsvp-event")
  private Set<Rsvp> rsvps = new HashSet<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "series_id")
  @JsonBackReference("event-series")
  private Series series;

  public Event(
      String title,
      String description,
      Timestamp startTimestamp,
      Integer durationMinutes,
      String imageUrl,
      String venue,
      String onlineLink,
      Series series) {
    this.title = title;
    this.description = description;
    this.startTimestamp = startTimestamp;
    this.durationMinutes = durationMinutes;
    this.imageUrl = imageUrl;
    this.venue = venue;
    this.onlineLink = onlineLink;
    this.series = series;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event Event)) return false;
    return Objects.equals(this.id, Event.id)
        && Objects.equals(this.title, Event.title)
        && Objects.equals(this.startTimestamp, Event.startTimestamp)
        && Objects.equals(this.durationMinutes, Event.durationMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.title, this.startTimestamp, this.durationMinutes);
  }

  @Override
  public String toString() {
    return "Event{"
        + "id="
        + this.id
        + ", title='"
        + this.title
        + "', startTimestamp='"
        + this.startTimestamp
        + "', durationMinutes='"
        + this.durationMinutes
        + "'}";
  }
}
