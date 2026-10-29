package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.external.response.WeatherSummaryDto;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.util.List;

public interface ForecastService {

  WeatherSummaryDto fetchAndSave(Long locationId,
      List<OpenWeatherParameters> parameters, Integer numberOfDays, Language language);
}
