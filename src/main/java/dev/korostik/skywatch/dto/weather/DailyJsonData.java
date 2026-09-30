package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record DailyJsonData(
    @JsonProperty("time") List<LocalDate> time,
    @JsonProperty("temperature_2m_max") List<Double> temperatureMax,
    @JsonProperty("temperature_2m_min") List<Double> temperatureMin,
    @JsonProperty("weather_code") List<String> weatherCode
) {

}
