package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.util.List;

public interface ForecastService {

  WeatherSummary fetchAndSave(Location location,
      List<OpenWeatherParameters> parameters, int numberOfDays);
}
