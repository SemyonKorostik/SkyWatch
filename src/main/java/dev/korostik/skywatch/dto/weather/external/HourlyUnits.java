package dev.korostik.skywatch.dto.weather.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record HourlyUnits(
    @JsonProperty("time") @NotNull String time,
    @JsonProperty("temperature_2m") @NotNull String temperature2m,
    @JsonProperty("relative_humidity_2m") @NotNull String relativeHumidity2m,
    @JsonProperty("weather_code") @NotNull String weatherCode,
    @JsonProperty("apparent_temperature") @NotNull String apparentTemperature,
    @JsonProperty("precipitation") @NotNull String precipitation,
    @JsonProperty("precipitation_probability") @NotNull String precipitationProbability,
    @JsonProperty("wind_speed_10m") @NotNull String windSpeed10m,
    @JsonProperty("wind_direction_10m") @NotNull String windDirection10m,
    @JsonProperty("surface_pressure") @NotNull String surfacePressure
) {

}