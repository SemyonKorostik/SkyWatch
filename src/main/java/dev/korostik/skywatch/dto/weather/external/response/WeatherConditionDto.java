package dev.korostik.skywatch.dto.weather.external.response;

public record WeatherConditionDto(
    WmoWeatherCodeDto wmoCode,
    Double temperature,
    Double windSpeed,
    Integer windDirection,
    Integer humidity,
    Double pressure
) {

}
