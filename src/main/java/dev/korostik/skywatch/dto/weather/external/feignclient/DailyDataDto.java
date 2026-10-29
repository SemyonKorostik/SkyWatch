package dev.korostik.skywatch.dto.weather.external.feignclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record DailyDataDto(
    @JsonProperty("time") @NotEmpty List<LocalDate> time,
    @JsonProperty("temperature_2m_max") @NotEmpty List<Double> temperatureMax,
    @JsonProperty("temperature_2m_min") @NotEmpty List<Double> temperatureMin,
    @JsonProperty("weather_code") @NotEmpty List<String> weatherCode
) {

}
