package dev.korostik.skywatch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "weather_provider")
public class WeatherProvider {

  @Id
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "api_url", nullable = false)
  private String apiUrl;

  @Column(name = "priority", nullable = false)
  private Integer priority;

  @Column(name = "is_enabled", nullable = false)
  private Boolean isEnabled;

}