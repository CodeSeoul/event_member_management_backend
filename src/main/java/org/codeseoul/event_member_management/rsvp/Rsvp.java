package org.codeseoul.event_member_management.rsvp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.event.Event;
import org.codeseoul.event_member_management.member.Member;
import org.codeseoul.event_member_management.rsvp_state.RsvpState;
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
public class Rsvp {
    private @Id @GeneratedValue Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private RsvpState state;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Rsvp(Member member, Event event, RsvpState state) {
        this.member = member;
        this.event = event;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Rsvp rsvp))
            return false;
        return Objects.equals(this.id, rsvp.id)
                && Objects.equals(this.member.getId(), rsvp.member.getId())
                && Objects.equals(this.event.getId(), rsvp.event.getId())
                && Objects.equals(this.state.getId(), rsvp.state.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.member.getId(), this.event.getId(), this.state.getId());
    }

    @Override
    public String toString() {
        return "Rsvp{" + "id=" + this.id + ", member='" + this.member.getUsername() + "', event='" + this.event.getTitle() + "', state='" + this.state.getState() + "'}";
    }
}
