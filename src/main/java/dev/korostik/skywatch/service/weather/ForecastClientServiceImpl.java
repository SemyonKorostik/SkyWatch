package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastClientServiceImpl implements ForecastClientService {

  private final ForecastService forecastService;

  @Override
  public String getCurrentWeather(Location location) {
    WeatherSummary weatherSummary = forecastService.fetchAndSave(location,
        List.of(OpenWeatherParameters.CURRENT), null);
    //преобразовать weatherSummary в соответствующий ChatTemplates
  }

  @Override
  public String getHourlyForecast(Location location) {
    return "";
  }

  @Override
  public String getDailyForecast(Location location) {
    return "";
  }

  @Override
  public String getWeeklyForecast(Location location) {
    return "";
  }
}
