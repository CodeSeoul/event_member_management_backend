package org.codeseoul.event_member_management.rsvp_state;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
public class RsvpState {
    private @Id @GeneratedValue Long id;

    private String state;

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public RsvpState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RsvpState state))
            return false;
        return Objects.equals(this.id, state.id)
                && Objects.equals(this.state, state.state);
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
