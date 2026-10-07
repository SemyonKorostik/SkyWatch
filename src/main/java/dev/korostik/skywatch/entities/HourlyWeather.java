package dev.korostik.skywatch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "hourly_weather")
public class HourlyWeather {

  @EmbeddedId
  private HourlyWeatherId id;

  @MapsId("locationId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "location_id", nullable = false)
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "weather_condition_id", nullable = false)
  private WeatherCondition weatherCondition;

  @Column(name = "temperature", nullable = false)
  private Double temperature;

  @Column(name = "wind_speed", nullable = false)
  private Double windSpeed;

  @Column(name = "humidity")
  private Integer humidity;

  @Column(name = "precipitation")
  private Double precipitation;

  @Column(name = "precipitation_probability")
  private Integer precipitationProbability;

  @Column(name = "wind_direction")
  private Integer windDirection;

  @Column(name = "pressure")
  private Double pressure;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}