package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DailyData(
    @JsonProperty("time") LocalDate time,
    @JsonProperty("temperature_2m_max") Double temperatureMax,
    @JsonProperty("temperature_2m_min") Double temperatureMin,
    @JsonProperty("weather_code") String weatherCode
) {}