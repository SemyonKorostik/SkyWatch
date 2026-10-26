package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.util.List;

public interface ForecastService {

  WeatherSummary fetchAndSave(Long locationId,
      List<OpenWeatherParameters> parameters, Integer numberOfDays, Language language);
}
