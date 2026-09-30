package dev.korostik.skywatch.entity;

import dev.korostik.skywatch.enums.ForecastApiProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumeratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@ToString
@Entity
@Table(name = "weather_provider")
public class WeatherProvider {
    @Id
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ForecastApiProvider provider;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_url", nullable = false)
    private String apiUrl;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;

}