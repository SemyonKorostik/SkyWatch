package dev.korostik.skywatch.entities.location;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "location_name")
public class LocationName {

  @EmbeddedId
  private LocationNameId id;

  @MapsId("locationId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @Column(name = "country", nullable = false)
  private String country;

  @Column(name = "place_name", nullable = false)
  private String placeName;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    LocationName that = (LocationName) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getId());
  }
}