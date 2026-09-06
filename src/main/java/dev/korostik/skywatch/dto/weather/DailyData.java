package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record DailyData(
    @JsonProperty("time") List<LocalDate> time,
    @JsonProperty("temperature_2m_max") List<Double> temperature2mMax,
    @JsonProperty("temperature_2m_min") List<Double> temperature2mMin,
    @JsonProperty("weather_code") List<Integer> weatherCode
) {}