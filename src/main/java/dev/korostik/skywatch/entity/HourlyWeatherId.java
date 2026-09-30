package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HourlyWeatherId implements Serializable {
    private static final long serialVersionUID = 6311297551880977047L;
    @NotNull
    @Column(name = "\"time\"", nullable = false)
    private Instant time;

    @NotNull
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        HourlyWeatherId entity = (HourlyWeatherId) o;
        return Objects.equals(this.locationId, entity.locationId) && Objects.equals(this.time, entity.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, time);
    }

}