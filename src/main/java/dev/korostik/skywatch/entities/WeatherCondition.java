package dev.korostik.skywatch.entities;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "weather_conditions")
public class WeatherCondition {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_conditions_id_gen")
  @SequenceGenerator(name = "weather_conditions_id_gen", sequenceName = "weather_conditions_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "provider_name", nullable = false)
  private WeatherProvider providerName;

  @Column(name = "provider_code", nullable = false, length = 10)
  private String providerCode;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "wmo_code", nullable = false)
  private WmoWeatherCode wmoCode;

}