package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record HourlyJsonData(
    @JsonProperty("time") List<LocalDateTime> time,
    @JsonProperty("temperature_2m") List<Double> temperature,
    @JsonProperty("relative_humidity_2m") List<Integer> relativeHumidity,
    @JsonProperty("weather_code") List<String> weatherCode,
    @JsonProperty("apparent_temperature") List<Double> apparentTemperature,
    @JsonProperty("precipitation") List<Double> precipitation,
    @JsonProperty("precipitation_probability") List<Integer> precipitationProbability,
    @JsonProperty("wind_speed_10m") List<Double> windSpeed,
    @JsonProperty("wind_direction_10m") List<Integer> windDirection,
    @JsonProperty("surface_pressure") List<Double> surfacePressure
) {

}
