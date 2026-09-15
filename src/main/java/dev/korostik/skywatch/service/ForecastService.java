package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.OpenWeatherApiClientProxy;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.mapper.WeatherMapper;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastService {

  private final OpenWeatherApiClientProxy openWeatherApiClientProxy;
  private final WeatherMapper weatherMapper;
  private final DailyWeatherRepository dailyWeatherRepository;

//  public HourlyWeather getHourlyForecast(Location location) {
//
//  }

  @Transactional
  public Iterable<DailyWeather> getDailyForecast(Location location, int numberOfDays) {
    return dailyWeatherRepository.saveAll(weatherMapper.toEntity(
        openWeatherApiClientProxy.getForecast(
            ForecastRequest.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .forecastDays(numberOfDays)
                .timezone(location.getTimeZoneId())
                .daily(OpenWeatherParameters.DAILY.getParameters())
                .build()
        )));
  }

}
