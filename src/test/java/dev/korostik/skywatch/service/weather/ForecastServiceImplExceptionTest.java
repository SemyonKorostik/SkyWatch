package dev.korostik.skywatch.service.weather;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.dto.weather.external.feignclient.ForecastResponse;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.exception.WeatherProviderException;
import dev.korostik.skywatch.exception.WeatherServiceException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class ForecastServiceImplExceptionTest {

  private ForecastApiProviderResolver resolver;
  private WeatherPersistenceService persistenceService;
  private WeatherConditionService conditionService;
  private ForecastServiceImpl service;

  @BeforeEach
  void setUp() {
    resolver = Mockito.mock(ForecastApiProviderResolver.class);
    persistenceService = Mockito.mock(WeatherPersistenceService.class);
    conditionService = Mockito.mock(WeatherConditionService.class);
    service = new ForecastServiceImpl(resolver, persistenceService, conditionService,
        Mockito.mock(dev.korostik.skywatch.mapper.struct.CurrentWeatherMapper.class),
        Mockito.mock(dev.korostik.skywatch.mapper.struct.DailyWeatherMapper.class),
        Mockito.mock(dev.korostik.skywatch.mapper.custom.DailyDataMapper.class),
        Mockito.mock(dev.korostik.skywatch.mapper.custom.HourlyDataMapper.class),
        Mockito.mock(dev.korostik.skywatch.mapper.struct.HourlyWeatherMapper.class));
  }

  @Nested
  @DisplayName("Exception paths")
  class ExceptionPaths {

    @Test
    @DisplayName("Should throw WeatherServiceException when no providers configured")
    void noProviders_throwsWeatherServiceException() {
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      Mockito.when(resolver.getAvailableProviders()).thenReturn(Collections.emptyList());

      assertThatThrownBy(
          () -> service.fetchAndSave(loc.getId(), List.of(OpenWeatherParameters.CURRENT), 1))
          .isInstanceOf(WeatherServiceException.class)
          .hasMessageContaining("No available weather providers configured");
    }

    @Test
    @DisplayName("Should throw immediately on non-retryable provider error")
    void nonRetryableError_throwsImmediately() {
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      ForecastApiClientProxy proxy = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(proxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(proxy));
      WeatherProviderException ex = new WeatherProviderException("Invalid params", HttpStatus.BAD_REQUEST, Map.of(), "OPEN_METEO");
      Mockito.when(proxy.getForecast(Mockito.any())).thenThrow(ex);

      assertThatThrownBy(
          () -> service.fetchAndSave(loc.getId(), List.of(OpenWeatherParameters.CURRENT), 1))
          .isInstanceOf(WeatherServiceException.class)
          .hasMessageContaining("non-retryable error");
    }

    @Test
    @DisplayName("Should fallback to next provider on retryable error")
    void retryableThenSuccess_fallbackWorks() {
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      ForecastApiClientProxy failingProxy = Mockito.mock(ForecastApiClientProxy.class);
      ForecastApiClientProxy successProxy = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(failingProxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(successProxy.getType()).thenReturn(ForecastApiProvider.OPEN_WEATHER);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(failingProxy, successProxy));
      Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_WEATHER))
          .thenReturn(Map.of("0", new WeatherCondition()));
      Mockito.when(persistenceService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

      WeatherProviderException retryable = new WeatherProviderException("Timeout", HttpStatus.SERVICE_UNAVAILABLE, Map.of(), "OPEN_METEO");
      Mockito.when(failingProxy.getForecast(Mockito.any())).thenThrow(retryable);
      Mockito.when(successProxy.getForecast(Mockito.any())).thenReturn(ForecastResponse.builder().build());

      service.fetchAndSave(loc.getId(), List.of(OpenWeatherParameters.CURRENT), 1);
      Mockito.verify(successProxy).getForecast(Mockito.any());
    }

    @Test
    @DisplayName("Should throw when all providers fail with retryable errors")
    void allRetryableFail_throwsWeatherServiceException() {
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      ForecastApiClientProxy proxy1 = Mockito.mock(ForecastApiClientProxy.class);
      ForecastApiClientProxy proxy2 = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(proxy1.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(proxy2.getType()).thenReturn(ForecastApiProvider.OPEN_WEATHER);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(proxy1, proxy2));
      WeatherProviderException retryable = new WeatherProviderException("Timeout", HttpStatus.SERVICE_UNAVAILABLE, Map.of(), "OPEN_METEO");
      Mockito.when(proxy1.getForecast(Mockito.any())).thenThrow(retryable);
      Mockito.when(proxy2.getForecast(Mockito.any())).thenThrow(retryable);

      assertThatThrownBy(
          () -> service.fetchAndSave(loc.getId(), List.of(OpenWeatherParameters.CURRENT), 1))
          .isInstanceOf(WeatherServiceException.class)
          .hasMessageContaining("All weather providers failed");
    }
  }
}
