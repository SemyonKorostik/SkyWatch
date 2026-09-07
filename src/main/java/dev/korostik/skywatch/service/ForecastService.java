package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.OpenWeatherApiClientProxy;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.Location;
import java.util.Date;
import java.util.List;
import java.util.Set;

import dev.korostik.skywatch.mapper.WeatherMapper;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
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

  public Iterable<DailyWeather> getDailyForecast(Location location, int numberOfDays) {
    return dailyWeatherRepository.saveAll(weatherMapper.toEntity(
            openWeatherApiClientProxy.getForecast(
                    ForecastRequest.builder()
                            .latitude(Double.valueOf(location.getLatitude()))
                            .longitude(Double.valueOf(location.getLongitude()))
                            .forecastDays(numberOfDays)
                            .timezone("Europe/Moscow")
                            .daily(List.of("temperature_2m_max",
                                    "temperature_2m_min",
                                    "weather_code"))
                            .build()
            )));
  }

}
