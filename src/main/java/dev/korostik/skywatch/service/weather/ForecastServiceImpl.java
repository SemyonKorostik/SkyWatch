package dev.korostik.skywatch.service.weather;

import static dev.korostik.skywatch.enums.OpenWeatherParameters.CURRENT;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.DAILY;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.HOURLY;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.dto.weather.transit.DailyData;
import dev.korostik.skywatch.dto.weather.external.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import dev.korostik.skywatch.dto.weather.publicApi.WeatherResponse;
import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.mapper.CurrentWeatherMapper;
import dev.korostik.skywatch.mapper.DailyDataMapper;
import dev.korostik.skywatch.mapper.DailyWeatherMapper;
import dev.korostik.skywatch.mapper.HourlyDataMapper;
import dev.korostik.skywatch.mapper.HourlyWeatherMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

  private final ForecastApiClientProxy forecastApiClientProxy;

  private final CurrentWeatherMapper currentWeatherMapper;
  private final DailyWeatherMapper dailyWeatherMapper;
  private final DailyDataMapper dailyDataMapper;
  private final HourlyDataMapper hourlyDataMapper;
  private final HourlyWeatherMapper hourlyWeatherMapper;

  private final WeatherPersistenceService weatherPersistenceService;
  private final ForecastApiProviderResolver forecastApiProviderResolver;

  public WeatherResponse fetchAndSave(Location location,
      List<OpenWeatherParameters> parameters, int numberOfDays) {
    ForecastResponse response = fetchForecast(location, parameters, numberOfDays);
    Map<String, WeatherCondition> providerCodes = forecastApiProviderResolver.getWithActualProvider();
    WeatherSummary weatherSummary = mapForecast(response, location, providerCodes);
    WeatherSummary saved = weatherPersistenceService.save(weatherSummary);
    return mapForecast(saved);
  }

  private ForecastResponse fetchForecast(Location location,
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

  private WeatherSummary mapForecast(ForecastResponse response, Location location,
      Map<String, WeatherCondition> providerCodes) {
    return WeatherSummary.builder()
        .currentWeather(Optional.ofNullable(response.current())
            .map(current -> currentWeatherMapper.toEntity(current, location, providerCodes))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(response.daily())
            .map(daily -> {
              List<DailyData> dailyData = dailyDataMapper.tolist(daily);
              return dailyWeatherMapper.toEntities(dailyData, location, providerCodes);
            })
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(response.hourly())
            .map(hourly -> {
              List<HourlyData> hourlyData = hourlyDataMapper.toList(hourly);
              return hourlyWeatherMapper.toEntities(hourlyData, location, providerCodes);
            })
            .orElse(null))
        .build();
  }

  private WeatherResponse mapForecast(WeatherSummary weatherSummary) {
    return WeatherResponse.builder()
        .currentWeather(currentWeatherMapper.toDto(weatherSummary.getCurrentWeather()))
        .dailyJsonData(dailyWeatherMapper.toDailyDataList(weatherSummary.getDailyWeather()))
        .hourlyJsonData(hourlyWeatherMapper.toHourlyDataList(weatherSummary.getHourlyWeather()))
        .build();
  }

}
