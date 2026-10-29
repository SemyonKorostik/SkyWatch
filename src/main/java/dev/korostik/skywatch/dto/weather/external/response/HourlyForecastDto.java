package dev.korostik.skywatch.dto.weather.external.response;

import java.time.Instant;

public record HourlyForecastDto(
    Instant time,
    WeatherConditionDto weatherCondition,
    Double temperature,
    Double windSpeed,
    Integer humidity,
    Double precipitation,
    Integer precipitationProbability,
    Integer windDirection,
    Double pressure
) {

}
