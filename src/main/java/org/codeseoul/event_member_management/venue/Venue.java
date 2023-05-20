/* (C) 2023 */
package org.codeseoul.event_member_management.venue;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeseoul.event_member_management.common.Auditable;

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

  public Venue(
      String name,
      String address,
      String naverMapsLink,
      String tmapLink,
      String kakaoMapsLink,
      String googleMapsLink) {
    this.name = name;
    this.address = address;
    this.naverMapsLink = naverMapsLink;
    this.tmapLink = tmapLink;
    this.kakaoMapsLink = kakaoMapsLink;
    this.googleMapsLink = googleMapsLink;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Venue venue)) return false;
    return Objects.equals(this.id, venue.id)
        && Objects.equals(this.name, venue.name)
        && Objects.equals(this.address, venue.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.address);
  }

  @Override
  public String toString() {
    return "Venue{"
        + "id="
        + this.id
        + ", name='"
        + this.name
        + "', address='"
        + this.address
        + "'}";
  }
}
