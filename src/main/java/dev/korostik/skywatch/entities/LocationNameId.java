package dev.korostik.skywatch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Embeddable
public class LocationNameId implements Serializable {

  private static final long serialVersionUID = -7385508043197187767L;
  @Column(name = "location_id", nullable = false)
  private Long locationId;

  @Column(name = "language", nullable = false, length = 2)
  private String language;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    LocationNameId entity = (LocationNameId) o;
    return Objects.equals(this.locationId, entity.locationId) &&
        Objects.equals(this.language, entity.language);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, language);
  }

}