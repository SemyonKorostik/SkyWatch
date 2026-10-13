package dev.korostik.skywatch.service.weather;

import static dev.korostik.skywatch.enums.OpenWeatherParameters.CURRENT;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.DAILY;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.HOURLY;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.dto.weather.WeatherSummary;
import dev.korostik.skywatch.dto.weather.WeatherSummaryDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.mapper.CurrentWeatherMapper;
import dev.korostik.skywatch.mapper.DailyDataMapper;
import dev.korostik.skywatch.mapper.DailyWeatherMapper;
import dev.korostik.skywatch.mapper.HourlyDataMapper;
import dev.korostik.skywatch.mapper.HourlyWeatherMapper;
import dev.korostik.skywatch.repository.CurrentWeatherRepository;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import dev.korostik.skywatch.repository.HourlyWeatherRepository;
import dev.korostik.skywatch.resolvers.ForecastApiProviderResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

  private final ForecastApiClientProxy forecastApiClientProxy;

  private final CurrentWeatherMapper currentWeatherMapper;
  private final DailyWeatherMapper dailyWeatherMapper;
  private final DailyDataMapper dailyDataMapper;
  private final HourlyDataMapper hourlyDataMapper;
  private final HourlyWeatherMapper hourlyWeatherMapper;

  private final CurrentWeatherRepository currentWeatherRepository;
  private final DailyWeatherRepository dailyWeatherRepository;
  private final HourlyWeatherRepository hourlyWeatherRepository;

  private final ForecastApiProviderResolver forecastApiProviderResolver;

  @Override
  public ForecastResponse fetchForecast(Location location,
      List<OpenWeatherParameters> parameters, int numberOfDays) {
    return forecastApiClientProxy.getForecast(
        ForecastRequest.builder()
            .latitude(location.getLatitude())
            .longitude(location.getLongitude())
            .forecastDays(numberOfDays)
            .zoneId(location.getTimeZoneId().getId())
            .current(parameters.contains(CURRENT) ? CURRENT.getParameters() : null)
            .hourly(parameters.contains(HOURLY) ? HOURLY.getParameters() : null)
            .daily(parameters.contains(DAILY) ? DAILY.getParameters() : null)
            .build()
    );
  }

  @Override
  public WeatherSummary mapForecast(ForecastResponse response, Location location) {
    return WeatherSummary.builder()
        .currentWeather(Optional.ofNullable(response.current())
            .map(current -> currentWeatherMapper.toEntity(current, location,
                forecastApiProviderResolver.getWithActualProvider()))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(response.daily())
            .map(daily -> {
              List<DailyData> dailyData = dailyDataMapper.toList(daily);
              return dailyWeatherMapper.toEntities(dailyData, location,
                  forecastApiProviderResolver.getWithActualProvider());
            })
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(response.hourly())
            .map(hourly -> {
              List<HourlyData> hourlyData = hourlyDataMapper.toList(hourly);
              return hourlyWeatherMapper.toEntities(hourlyData, location,
                  forecastApiProviderResolver.getWithActualProvider());
            })
            .orElse(null))
        .build();
  }

  @Transactional
  @Override
  public WeatherSummaryDto saveForecast(WeatherSummary weatherSummary) {
    return WeatherSummaryDto.builder()
        .currentWeather(Optional.ofNullable(weatherSummary.getCurrentWeather())
            .map(current -> currentWeatherRepository.save(weatherSummary.getCurrentWeather()))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(weatherSummary.getDailyWeather())
            .map(daily -> dailyWeatherRepository.saveAll(weatherSummary.getDailyWeather())
                .stream()
                .collect(Collectors.toUnmodifiableMap(x -> x.getId().getDate(), x -> x)))
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(weatherSummary.getHourlyWeather())
            .map(hourly -> hourlyWeatherRepository.saveAll(weatherSummary.getHourlyWeather())
                .stream()
                .collect(Collectors.toUnmodifiableMap(x -> x.getId().getTime(), x -> x)))
            .orElse(null))
        .build();

  }

}
