package dev.korostik.skywatch.telegram.chat.template.enums;

import java.time.LocalDateTime;

public record HourlyParameters(
    LocalDateTime time,
    double temp,
    String condition,
    String icon,
    int precipitationChance,   // %
    double windSpeed           // km/h
) {}
