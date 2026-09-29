package dev.korostik.skywatch.telegram.chat.template.enums;

import java.time.LocalDateTime;

public record CurrentParameters(
    String location,
    double temp,
    double feelsLike,
    double tempMin,
    double tempMax,
    String condition,      // "Partly Cloudy"
    String icon,           // "⛅"
    int humidity,          // %
    double windSpeed,      // km/h
    String windDir,        // "NW"
    double pressure,       // hPa
    double visibility,     // km
    double uvIndex,
    String sunrise,        // "06:30"
    String sunset,         // "19:45"
    LocalDateTime updatedAt
) {
}
