package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentData(
    @JsonProperty("time") String time,
    @JsonProperty("interval") Integer interval,
    @JsonProperty("temperature_2m") Double temperature2m,
    @JsonProperty("relative_humidity_2m") Integer relativeHumidity2m,
    @JsonProperty("weather_code") Integer weatherCode,
    @JsonProperty("apparent_temperature") Double apparentTemperature,
    @JsonProperty("wind_speed_10m") Double windSpeed10m,
    @JsonProperty("wind_direction_10m") Integer windDirection10m,
    @JsonProperty("surface_pressure") Double surfacePressure
) {}