package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;

public interface WeatherPersistenceService {

  WeatherSummary save(WeatherSummary weatherSummary);
}
