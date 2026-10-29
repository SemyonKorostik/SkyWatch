package dev.korostik.skywatch.dto.weather.external.response;

public record CurrentWeatherDto(
    WeatherConditionDto weatherConditionDto,
    Double temperature,
    Double windSpeed,
    Integer windDirection,
    Integer humidity,
    Double pressure
) {

}
