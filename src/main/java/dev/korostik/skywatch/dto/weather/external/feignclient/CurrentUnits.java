package dev.korostik.skywatch.dto.weather.external.feignclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CurrentUnits(
    @JsonProperty("time") @NotNull String time,
    @JsonProperty("interval") @NotNull String interval,
    @JsonProperty("temperature_2m") @NotNull String temperature2m,
    @JsonProperty("relative_humidity_2m") @NotNull String relativeHumidity2m,
    @JsonProperty("weather_code") @NotNull String weatherCode,
    @JsonProperty("apparent_temperature") @NotNull String apparentTemperature,
    @JsonProperty("wind_speed_10m") @NotNull String windSpeed10m,
    @JsonProperty("wind_direction_10m") @NotNull String windDirection10m,
    @JsonProperty("surface_pressure") @NotNull String surfacePressure
) {

}