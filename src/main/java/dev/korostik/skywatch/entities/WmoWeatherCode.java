package dev.korostik.skywatch.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "wmo_weather_codes")
public class WmoWeatherCode {

  @Id
  @Column(name = "code", nullable = false, length = 2)
  private String code;

  @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
  private String description;

  @Column(name = "category", nullable = false, length = 50)
  private String category;

  @Column(name = "subcategory", length = 100)
  private String subcategory;

  @Column(name = "emoji", nullable = false, length = 10)
  private String emoji;

  @Column(name = "intensity", length = 20)
  private String intensity;

  @Column(name = "weather_group", nullable = false, length = 30)
  private String weatherGroup;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}