package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.ToString.Exclude;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_weather")
public class DailyWeather {
    //составной ключ location_id, date
    @EmbeddedId
    private DailyWeatherId id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "weather_condition_id", nullable = false)
    @Exclude
    private WeatherCondition weatherCondition;

    @NotNull
    @Column(name = "temperature_max", nullable = false)
    private Double temperatureMax;

    @NotNull
    @Column(name = "temperature_min", nullable = false)
    private Double temperatureMin;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}