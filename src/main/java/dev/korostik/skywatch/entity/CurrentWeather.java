package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "current_weather")
public class CurrentWeather {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "current_weather_seq")
  @SequenceGenerator(
      name = "current_weather_seq",
      sequenceName = "current_weather_id_seq",
      allocationSize = 1
  )
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "weather_condition_id", nullable = false)
  private WeatherCondition weatherCondition;

  @NotNull
  @Column(name = "temperature", nullable = false, precision = 4, scale = 1)
  private BigDecimal temperature;

  @NotNull
  @Column(name = "humidity", nullable = false)
  private Short humidity;

  @Column(name = "precipitation", precision = 5, scale = 2)
  private BigDecimal precipitation;

  @Column(name = "precipitation_probability")
  private Short precipitationProbability;

  @Column(name = "wind_speed", precision = 5, scale = 2)
  private BigDecimal windSpeed;

  @Column(name = "wind_direction")
  private Short windDirection;

  @Column(name = "pressure", precision = 6, scale = 2)
  private BigDecimal pressure;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;
}
