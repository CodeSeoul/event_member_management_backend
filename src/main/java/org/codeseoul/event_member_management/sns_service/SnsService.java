package org.codeseoul.event_member_management.sns_service;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
public class SnsService {
    private @Id @GeneratedValue Long id;

    @Column(unique = true)
    private String name;

    private String iconUrl;

    public SnsService(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SnsService service))
            return false;
        return Objects.equals(this.id, service.id)
                && Objects.equals(this.name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "SnsService{" + "id=" + this.id + ", name='" + this.name + "'}";
    }
}
