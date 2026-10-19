package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.dto.weather.domain.ProviderResult;
import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.dto.weather.external.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.exception.WeatherProviderException;
import dev.korostik.skywatch.exception.WeatherServiceException;
import dev.korostik.skywatch.mapper.custom.DailyDataMapper;
import dev.korostik.skywatch.mapper.custom.HourlyDataMapper;
import dev.korostik.skywatch.mapper.struct.CurrentWeatherMapper;
import dev.korostik.skywatch.mapper.struct.DailyWeatherMapper;
import dev.korostik.skywatch.mapper.struct.HourlyWeatherMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {

  private final ForecastApiProviderResolver providerResolver;
  private final WeatherPersistenceService weatherPersistenceService;
  private final WeatherConditionService weatherConditionService;

  private final CurrentWeatherMapper currentWeatherMapper;
  private final DailyWeatherMapper dailyWeatherMapper;
  private final DailyDataMapper dailyDataMapper;
  private final HourlyDataMapper hourlyDataMapper;
  private final HourlyWeatherMapper hourlyWeatherMapper;

  @Override
  @Transactional
  public WeatherSummary fetchAndSave(Location location, List<OpenWeatherParameters> parameters, int numberOfDays) {
    ProviderResult providerResult = fetchForecastWithFallback(location, parameters, numberOfDays);
    Map<String, WeatherCondition> providerCodes = weatherConditionService.getAllByProvider(providerResult.provider());
    WeatherSummary weatherSummary = mapForecast(providerResult.response(), location, providerCodes);
    return weatherPersistenceService.save(weatherSummary);
  }

  private ProviderResult fetchForecastWithFallback(Location location, List<OpenWeatherParameters> parameters, int numberOfDays) {
    ForecastRequest request = buildRequest(location, parameters, numberOfDays);
    List<ForecastApiClientProxy> providers = providerResolver.getAvailableProviders();

    if (providers.isEmpty()) {
      throw new WeatherServiceException("No available weather providers configured", List.of());
    }

    List<WeatherProviderException> errors = new java.util.ArrayList<>();

    for (ForecastApiClientProxy provider : providers) {
      try {
        log.debug("Fetching forecast from provider: {}", provider.getType());
        return new ProviderResult(provider.getType(), provider.getForecast(request));
      } catch (WeatherProviderException e) {
        errors.add(e);
        log.warn("Provider {} failed with status {} (retryable: {}): {}", 
            provider.getType(), e.getStatus(), e.isRetryable(), e.getMessage());

        if (!e.isRetryable()) {
          throw new WeatherServiceException("Provider %s returned non-retryable error".formatted(provider.getType()), errors);
        }
      }
    }

    throw new WeatherServiceException("All weather providers failed", errors);
  }

  private ForecastRequest buildRequest(Location location, List<OpenWeatherParameters> parameters, int numberOfDays) {
    return ForecastRequest.builder()
        .latitude(location.getLatitude())
        .longitude(location.getLongitude())
        .forecastDays(numberOfDays)
        .zoneId(location.getTimeZoneId().getId())
        .current(parameters.contains(OpenWeatherParameters.CURRENT) ? OpenWeatherParameters.CURRENT.getParameters() : null)
        .hourly(parameters.contains(OpenWeatherParameters.HOURLY) ? OpenWeatherParameters.HOURLY.getParameters() : null)
        .daily(parameters.contains(OpenWeatherParameters.DAILY) ? OpenWeatherParameters.DAILY.getParameters() : null)
        .build();
  }

  private WeatherSummary mapForecast(ForecastResponse response, Location location, Map<String, WeatherCondition> providerCodes) {
    return WeatherSummary.builder()
        .currentWeather(Optional.ofNullable(response.current())
            .map(current -> currentWeatherMapper.toEntity(current, location, providerCodes))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(response.daily())
            .map(daily -> dailyWeatherMapper.toEntities(dailyDataMapper.toList(daily), location, providerCodes))
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(response.hourly())
            .map(hourly -> hourlyWeatherMapper.toEntities(hourlyDataMapper.toList(hourly), location, providerCodes))
            .orElse(null))
        .build();
  }
}
