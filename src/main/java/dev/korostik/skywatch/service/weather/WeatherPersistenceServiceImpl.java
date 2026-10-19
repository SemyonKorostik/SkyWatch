package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.repository.CurrentWeatherRepository;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import dev.korostik.skywatch.repository.HourlyWeatherRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherPersistenceServiceImpl implements WeatherPersistenceService {

  private final CurrentWeatherRepository currentWeatherRepository;
  private final DailyWeatherRepository dailyWeatherRepository;
  private final HourlyWeatherRepository hourlyWeatherRepository;

  @Override
  public WeatherSummary save(WeatherSummary weatherSummary) {
    return WeatherSummary.builder()
        .currentWeather(Optional.ofNullable(weatherSummary.getCurrentWeather())
            .map(currentWeatherRepository::save)
            .orElse(null))
        .dailyWeather(Optional.ofNullable(weatherSummary.getDailyWeather())
            .map(dailyWeatherRepository::saveAll)
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(weatherSummary.getHourlyWeather())
            .map(hourlyWeatherRepository::saveAll)
            .orElse(null))
        .build();
  }

}
