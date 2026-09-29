package dev.korostik.skywatch.telegram.chat.template.enums;

import java.time.LocalDate;

public record DailyParameters(
    LocalDate date,
    double tempMin,
    double tempMax,
    String condition,
    String icon,
    int precipitationChance,
    double windSpeed,
    String sunrise,
    String sunset
) {}
