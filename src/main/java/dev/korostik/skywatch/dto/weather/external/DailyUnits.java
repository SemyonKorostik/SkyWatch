package dev.korostik.skywatch.dto.weather.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record DailyUnits(
    @JsonProperty("time") @NotNull String time,
    @JsonProperty("temperature_2m_max") @NotNull String temperature2mMax,
    @JsonProperty("temperature_2m_min") @NotNull String temperature2mMin,
    @JsonProperty("weather_code") @NotNull String weatherCode
) {

}
