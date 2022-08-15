package org.codeseoul.event_member_management.venue;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
public class Venue extends Auditable {
    private @Id @GeneratedValue Long id;
    private String name;
    private String address;
    private String naverMapsLink;
    private String tmapLink;
    private String kakaoMapsLink;
    private String googleMapsLink;

    public Venue(String name, String address, String naverMapsLink, String tmapLink, String kakaoMapsLink, String googleMapsLink) {
        this.name = name;
        this.address = address;
        this.naverMapsLink = naverMapsLink;
        this.tmapLink = tmapLink;
        this.kakaoMapsLink = kakaoMapsLink;
        this.googleMapsLink = googleMapsLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Venue Venue))
            return false;
        return Objects.equals(this.id, Venue.id)
                && Objects.equals(this.name, Venue.name)
                && Objects.equals(this.address, Venue.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.address);
    }

    @Override
    public String toString() {
        return "Venue{" + "id=" + this.id + ", name='" + this.name + "', address='" + this.address + "'}";
    }
}
