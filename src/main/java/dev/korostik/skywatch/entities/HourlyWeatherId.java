package dev.korostik.skywatch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.Instant;
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
public class HourlyWeatherId implements Serializable {

  private static final long serialVersionUID = 2504266096596704660L;
  @Column(name = "\"time\"", nullable = false)
  private Instant time;

  @Column(name = "location_id", nullable = false)
  private Long locationId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    HourlyWeatherId entity = (HourlyWeatherId) o;
    return Objects.equals(this.locationId, entity.locationId) &&
        Objects.equals(this.time, entity.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, time);
  }

}