package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public record HourlyData (
    @JsonProperty("time") LocalDateTime time,
    @JsonProperty("temperature_2m") Double temperature2m,
    @JsonProperty("relative_humidity_2m") Integer relativeHumidity2m,
    @JsonProperty("weather_code") String weatherCode,
    @JsonProperty("apparent_temperature") Double apparentTemperature,
    @JsonProperty("precipitation") Double precipitation,
    @JsonProperty("precipitation_probability") Integer precipitationProbability,
    @JsonProperty("wind_speed_10m") Double windSpeed10m,
    @JsonProperty("wind_direction_10m") Integer windDirection10m,
    @JsonProperty("surface_pressure") Double surfacePressure
) {}
