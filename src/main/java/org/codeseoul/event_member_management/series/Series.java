package org.codeseoul.event_member_management.series;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.event.Event;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
public class Series {
    private @Id @GeneratedValue Long id;
    private String name;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Event> events = new ArrayList<>();

    @Column(insertable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(insertable = false, updatable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Series(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Series series))
            return false;
        return Objects.equals(this.id, series.id)
                && Objects.equals(this.name, series.name);
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
