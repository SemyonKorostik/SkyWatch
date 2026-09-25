package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record DailyData(
    @JsonProperty("time") LocalDate time,
    @JsonProperty("temperature_2m_max") Double temperature2mMax,
    @JsonProperty("temperature_2m_min") Double temperature2mMin,
    @JsonProperty("weather_code") String weatherCode
) {}