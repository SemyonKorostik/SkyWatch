package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hourly_weather")
public class HourlyWeather {
    @EmbeddedId
    private HourlyWeatherId id;

    @MapsId("dailyWeatherId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "daily_weather_id", nullable = false)
    private DailyWeather dailyWeather;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "weather_condition_id", nullable = false)
    private WeatherCondition weatherCondition;

    @NotNull
    @Column(name = "temperature", nullable = false, precision = 4, scale = 1)
    private Double temperature;

    @NotNull
    @Column(name = "humidity", nullable = false)
    private Integer humidity;

    @Column(name = "precipitation", precision = 5, scale = 2)
    private Double precipitation;

    @Column(name = "precipitation_probability")
    private Integer precipitationProbability;

    @Column(name = "wind_speed", precision = 5, scale = 2)
    private Double windSpeed;

    @Column(name = "wind_direction")
    private Integer windDirection;

    @Column(name = "pressure", precision = 6, scale = 2)
    private Double pressure;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}