package dev.korostik.skywatch.dto.weather.transit;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record DailyData(
    LocalDate date,
    Double temperatureMax,
    Double temperatureMin,
    String weatherCode
) {

}