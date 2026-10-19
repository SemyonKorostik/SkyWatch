package dev.korostik.skywatch.dto.weather.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CurrentDataDto(
    @JsonProperty("time") @NotNull LocalDateTime time,
    @JsonProperty("interval") @NotNull Integer interval,
    @JsonProperty("temperature_2m") @NotNull Double temperature,
    @JsonProperty("relative_humidity_2m") @NotNull Integer relativeHumidity,
    @JsonProperty("weather_code") @NotNull String weatherCode,
    @JsonProperty("apparent_temperature") @NotNull Double apparentTemperature,
    @JsonProperty("wind_speed_10m") @NotNull Double windSpeed,
    @JsonProperty("wind_direction_10m") @NotNull Integer windDirection,
    @JsonProperty("surface_pressure") @NotNull Double surfacePressure
) {

}