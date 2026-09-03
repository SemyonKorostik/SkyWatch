package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentUnits(
    @JsonProperty("time") String time,
    @JsonProperty("interval") String interval,
    @JsonProperty("temperature_2m") String temperature2m,
    @JsonProperty("relative_humidity_2m") String relativeHumidity2m,
    @JsonProperty("weather_code") String weatherCode,
    @JsonProperty("apparent_temperature") String apparentTemperature,
    @JsonProperty("wind_speed_10m") String windSpeed10m,
    @JsonProperty("wind_direction_10m") String windDirection10m,
    @JsonProperty("surface_pressure") String surfacePressure
) {}