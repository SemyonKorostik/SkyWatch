package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.dto.weather.WeatherSummary;
import dev.korostik.skywatch.dto.weather.WeatherSummaryDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.util.List;

public interface ForecastService {

  ForecastResponse fetchForecast(Location location,
      List<OpenWeatherParameters> parameters, int numberOfDays);

  WeatherSummary mapForecast(ForecastResponse response, Location location);

  WeatherSummaryDto saveForecast(WeatherSummary weatherSummary);
}
