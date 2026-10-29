package dev.korostik.skywatch.dto.weather.external.response;

import java.time.Instant;

public record CurrentWeatherDto(
    WeatherConditionDto weatherCondition,
    Double temperature,
    Double windSpeed,
    Integer windDirection,
    Integer humidity,
    Double pressure,
    Instant updatedAt
) {

}
