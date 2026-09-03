package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.OpenWeatherApiClientProxy;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.entity.Location;
import java.util.Date;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastService {
  private final OpenWeatherApiClientProxy openWeatherApiClientProxy;

  public Weather getForecast(Location location, Set<ForecastType> forecastTypes) {
  }

  public Weather getHourlyForecast(Location location) {

  }

  public Weather getDailyForecast(Location location) {

  }

}
