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
public class DailyWeatherId implements Serializable {

  private static final long serialVersionUID = 149154360072988127L;
  @Column(name = "location_id", nullable = false)
  private Long locationId;

  @Column(name = "date", nullable = false)
  private Instant date;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DailyWeatherId entity = (DailyWeatherId) o;
    return Objects.equals(this.date, entity.date) &&
        Objects.equals(this.locationId, entity.locationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, locationId);
  }

}