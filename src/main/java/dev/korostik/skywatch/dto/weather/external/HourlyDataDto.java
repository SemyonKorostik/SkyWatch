package dev.korostik.skywatch.dto.weather.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record HourlyDataDto(
    @JsonProperty("time") @NotEmpty List<LocalDateTime> time,
    @JsonProperty("temperature_2m") @NotEmpty List<Double> temperature,
    @JsonProperty("relative_humidity_2m") @NotEmpty List<Integer> relativeHumidity,
    @JsonProperty("weather_code") @NotEmpty List<String> weatherCode,
    @JsonProperty("apparent_temperature") @NotEmpty List<Double> apparentTemperature,
    @JsonProperty("precipitation") @NotEmpty List<Double> precipitation,
    @JsonProperty("precipitation_probability") @NotEmpty List<Integer> precipitationProbability,
    @JsonProperty("wind_speed_10m") @NotEmpty List<Double> windSpeed,
    @JsonProperty("wind_direction_10m") @NotEmpty List<Integer> windDirection,
    @JsonProperty("surface_pressure") @NotEmpty List<Double> surfacePressure
) {

}
