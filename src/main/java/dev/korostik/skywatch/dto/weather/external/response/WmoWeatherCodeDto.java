package dev.korostik.skywatch.dto.weather.external.response;

public record WmoWeatherCodeDto(
    String code, String description, String category, String subcategory, String emoji,
    String intensity, String weatherGroup) {

}
