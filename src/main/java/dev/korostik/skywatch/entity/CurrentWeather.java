package dev.korostik.skywatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


}
