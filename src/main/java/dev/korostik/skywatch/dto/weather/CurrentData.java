package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentData(
    @JsonProperty("time") String time,
    @JsonProperty("interval") Integer interval,
    @JsonProperty("temperature_2m") Double temperature,
    @JsonProperty("relative_humidity_2m") Integer relativeHumidity,
    @JsonProperty("weather_code") String weatherCode,
    @JsonProperty("apparent_temperature") Double apparentTemperature,
    @JsonProperty("wind_speed_10m") Double windSpeed,
    @JsonProperty("wind_direction_10m") Integer windDirection,
    @JsonProperty("surface_pressure") Double surfacePressure
) {}