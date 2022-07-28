package org.codeseoul.event_member_management.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.rsvp.Rsvp;
import org.codeseoul.event_member_management.series.Series;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class Event {
    private @Id @GeneratedValue Long id;
    private String title;
    private String description;
    private Timestamp startTimestamp;
    private Integer durationMinutes;
    private String imageUrl;
    private String venue;
    private String onlineLink;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Rsvp> rsvps;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Event(String title, String description, Timestamp startTimestamp, Integer durationMinutes, String imageUrl, String venue, String onlineLink, Series series) {
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
        if (this == o)
            return true;
        if (!(o instanceof Event Event))
            return false;
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
        return "Event{" + "id=" + this.id + ", title='" + this.title + "', startTimestamp='" + this.startTimestamp + "', durationMinutes='" + this.durationMinutes + "'}";
    }
}
