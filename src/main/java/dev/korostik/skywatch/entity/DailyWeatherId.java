package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DailyWeatherId implements Serializable {
    private static final long serialVersionUID = 6311297551880977047L;
    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DailyWeatherId entity = (DailyWeatherId) o;
        return Objects.equals(this.locationId, entity.locationId) && Objects.equals(this.date, entity.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, date);
    }

}