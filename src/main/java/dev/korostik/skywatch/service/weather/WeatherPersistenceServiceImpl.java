package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.repository.CurrentWeatherRepository;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import dev.korostik.skywatch.repository.HourlyWeatherRepository;
import java.util.List;
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
        .currentWeather(saveCurrentWeather(weatherSummary.getCurrentWeather()))
        .dailyWeather(Optional.ofNullable(weatherSummary.getDailyWeather())
            .map(dailyWeatherRepository::saveAll)
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(weatherSummary.getHourlyWeather())
            .map(hourlyWeatherRepository::saveAll)
            .orElse(null))
        .build();
  }

  private CurrentWeather saveCurrentWeather(CurrentWeather currentWeather) {
    if (currentWeather == null) {
      return null;
    }

    Long locationId = currentWeather.getLocation().getId();
    CurrentWeather existing = currentWeatherRepository.findById(locationId).orElse(null);

    if (existing != null) {
      // Update existing record with new values
      existing.setWeatherCondition(currentWeather.getWeatherCondition());
      existing.setTemperature(currentWeather.getTemperature());
      existing.setWindSpeed(currentWeather.getWindSpeed());
      existing.setWindDirection(currentWeather.getWindDirection());
      existing.setHumidity(currentWeather.getHumidity());
      existing.setPressure(currentWeather.getPressure());
      return currentWeatherRepository.save(existing);
    } else {
      // Insert new record
      return currentWeatherRepository.save(currentWeather);
    }
  }
}
