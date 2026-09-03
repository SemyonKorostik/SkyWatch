package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyUnits(
    @JsonProperty("time") String time,
    @JsonProperty("temperature_2m_max") String temperature2mMax,
    @JsonProperty("temperature_2m_min") String temperature2mMin,
    @JsonProperty("weather_code") String weatherCode
) {}
