package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "wmo_weather_codes")
public class WmoWeatherCode {
    @Id
    @ColumnDefault("nextval('wmo_weather_codes_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 2)
    @NotNull
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 50)
    @NotNull
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Size(max = 100)
    @Column(name = "subcategory", length = 100)
    private String subcategory;

    @Size(max = 10)
    @NotNull
    @Column(name = "emoji", nullable = false, length = 10)
    private String emoji;

    @Size(max = 20)
    @Column(name = "intensity", length = 20)
    private String intensity;

    @Size(max = 30)
    @NotNull
    @Column(name = "weather_group", nullable = false, length = 30)
    private String weatherGroup;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "wmoCode")
    private Set<WeatherCondition> weatherConditions = new LinkedHashSet<>();

}