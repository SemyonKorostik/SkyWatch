package dev.korostik.skywatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@ToString
@Entity
@Table(name = "weather_conditions")
public class WeatherCondition {
    @Id
    @ColumnDefault("nextval('weather_conditions_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "weather_provider_name", nullable = false)
    private WeatherProvider weatherProviderName;

    @NotNull
    @Column(name = "code", nullable = false)
    private Short code;

    @Size(max = 100)
    @NotNull
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Size(max = 50)
    @NotNull
    @Column(name = "icon_name", nullable = false, length = 50)
    private String iconName;

}