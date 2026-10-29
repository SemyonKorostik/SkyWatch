package dev.korostik.skywatch.dto.weather.external.response;

import java.time.Instant;

public record DailyForecastDto(
    Instant date,
    WeatherConditionDto weatherCondition,
    Double temperatureMax,
    Double temperatureMin,
    Double windSpeed,
    Integer windDirection,
    Integer humidity
) {

}
