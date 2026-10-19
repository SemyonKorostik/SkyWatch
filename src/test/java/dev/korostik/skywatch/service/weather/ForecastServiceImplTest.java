package dev.korostik.skywatch.service.weather;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.dto.weather.external.CurrentDataDto;
import dev.korostik.skywatch.dto.weather.external.DailyDataDto;
import dev.korostik.skywatch.dto.weather.external.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.dto.weather.external.HourlyDataDto;
import dev.korostik.skywatch.dto.weather.transit.DailyData;
import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class ForecastServiceImplTest {

  private ForecastApiProviderResolver resolver;
  private WeatherPersistenceService persistenceService;
  private WeatherConditionService conditionService;
  private ForecastServiceImpl service;

  private dev.korostik.skywatch.mapper.struct.CurrentWeatherMapper currentWeatherMapper;
  private dev.korostik.skywatch.mapper.struct.DailyWeatherMapper dailyWeatherMapper;
  private dev.korostik.skywatch.mapper.custom.DailyDataMapper dailyDataMapper;
  private dev.korostik.skywatch.mapper.custom.HourlyDataMapper hourlyDataMapper;
  private dev.korostik.skywatch.mapper.struct.HourlyWeatherMapper hourlyWeatherMapper;

  @BeforeEach
  void setUp() {
    resolver = Mockito.mock(ForecastApiProviderResolver.class);
    persistenceService = Mockito.mock(WeatherPersistenceService.class);
    conditionService = Mockito.mock(WeatherConditionService.class);
    currentWeatherMapper = Mockito.mock(
        dev.korostik.skywatch.mapper.struct.CurrentWeatherMapper.class);
    dailyWeatherMapper = Mockito.mock(dev.korostik.skywatch.mapper.struct.DailyWeatherMapper.class);
    dailyDataMapper = Mockito.mock(dev.korostik.skywatch.mapper.custom.DailyDataMapper.class);
    hourlyDataMapper = Mockito.mock(dev.korostik.skywatch.mapper.custom.HourlyDataMapper.class);
    hourlyWeatherMapper = Mockito.mock(
        dev.korostik.skywatch.mapper.struct.HourlyWeatherMapper.class);

    service = new ForecastServiceImpl(resolver, persistenceService, conditionService,
        currentWeatherMapper, dailyWeatherMapper, dailyDataMapper, hourlyDataMapper,
        hourlyWeatherMapper);
  }

  @Nested
  @DisplayName("fetchAndSave")
  class FetchAndSaveTests {

    @Test
    @DisplayName("Should save mapped forecast with all data present")
    void fetchAndSave_happyPath() {
      // given
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      ForecastResponse response = ForecastResponse.builder()
          .latitude(53.9)
          .longitude(27.56)
          .zoneId(ZoneId.of("Europe/Minsk"))
          .current(new CurrentDataDto(
              LocalDateTime.of(2024, 1, 15, 12, 0, 0), 3600, 5.0, 80, "0", 4.5, 3.5, 270, 1013.0))
          .daily(new DailyDataDto(
              List.of(LocalDate.of(2024, 6, 1)),
              List.of(8.0), List.of(-2.0), List.of("1")))
          .hourly(new HourlyDataDto(
              List.of(LocalDateTime.of(2024, 6, 1, 12, 0)),
              List.of(5.0), List.of(80), List.of("0"), List.of(4.5),
              List.of(0.0), List.of(10), List.of(3.5), List.of(270), List.of(1013.0)))
          .build();
      ForecastApiClientProxy mockProxy = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(mockProxy.getForecast(Mockito.any())).thenReturn(response);
      Mockito.when(mockProxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(mockProxy));
      Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
          .thenReturn(Map.of("0", new WeatherCondition()));
      Mockito.when(persistenceService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

      Mockito.when(currentWeatherMapper.toEntity(Mockito.any(), Mockito.any(), Mockito.any()))
          .thenReturn(CurrentWeather.builder().build());
      Mockito.when(dailyDataMapper.toList(Mockito.any()))
          .thenReturn(List.of(DailyData.builder()
              .date(LocalDate.of(2024, 6, 1)).build()));
      Mockito.when(dailyWeatherMapper.toEntities(Mockito.any(), Mockito.any(), Mockito.any()))
          .thenReturn(List.of(DailyWeather.builder().build()));
      Mockito.when(hourlyDataMapper.toList(Mockito.any()))
          .thenReturn(List.of(HourlyData.builder()
              .time(LocalDateTime.of(2024, 6, 1, 12, 0)).build()));
      Mockito.when(hourlyWeatherMapper.toEntities(Mockito.any(), Mockito.any(), Mockito.any()))
          .thenReturn(List.of(HourlyWeather.builder().build()));

      // when
      WeatherSummary result = service.fetchAndSave(loc,
          List.of(OpenWeatherParameters.CURRENT, OpenWeatherParameters.HOURLY,
              OpenWeatherParameters.DAILY), 3);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getCurrentWeather()).isNotNull();
      assertThat(result.getDailyWeather()).isNotNull();
      assertThat(result.getHourlyWeather()).isNotNull();
      Mockito.verify(persistenceService).save(Mockito.any(WeatherSummary.class));
    }

    @Test
    @DisplayName("Should pass correct ForecastRequest to provider")
    void fetchAndSave_shouldBuildCorrectRequest() {
      // given
      Location loc = Location.builder()
          .latitude(40.0).longitude(-74.0).timeZoneId(ZoneId.of("America/New_York")).build();
      ForecastApiClientProxy mockProxy = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(mockProxy.getForecast(Mockito.any()))
          .thenReturn(ForecastResponse.builder().build());
      Mockito.when(mockProxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(mockProxy));
      Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
              .thenReturn(Map.of());
      Mockito.when(persistenceService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

      // when
      service.fetchAndSave(loc, List.of(OpenWeatherParameters.CURRENT), 5);

      // then
      ArgumentCaptor<ForecastRequest> captor = ArgumentCaptor.forClass(ForecastRequest.class);
      Mockito.verify(mockProxy).getForecast(captor.capture());
      ForecastRequest request = captor.getValue();
      assertThat(request.latitude()).isEqualTo(40.0);
      assertThat(request.longitude()).isEqualTo(-74.0);
      assertThat(request.zoneId()).isEqualTo("America/New_York");
      assertThat(request.forecastDays()).isEqualTo(5);
      assertThat(request.current()).isNotNull();
      assertThat(request.hourly()).isNull();
      assertThat(request.daily()).isNull();
    }

    @Test
    @DisplayName("Should handle null current, daily and hourly in response")
    void fetchAndSave_nullResponseFields_shouldProduceNullSummaryFields() {
      // given
      Location loc = Location.builder()
          .latitude(53.9).longitude(27.56).timeZoneId(ZoneId.of("Europe/Minsk")).build();
      ForecastResponse response = ForecastResponse.builder()
          .latitude(53.9).longitude(27.56).zoneId(ZoneId.of("Europe/Minsk"))
          .current(null).daily(null).hourly(null)
          .build();
      ForecastApiClientProxy mockProxy = Mockito.mock(ForecastApiClientProxy.class);
      Mockito.when(mockProxy.getForecast(Mockito.any())).thenReturn(response);
      Mockito.when(mockProxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
      Mockito.when(resolver.getAvailableProviders()).thenReturn(List.of(mockProxy));
      Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
          .thenReturn(Map.of("0", new WeatherCondition()));
      Mockito.when(persistenceService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

      // when
      WeatherSummary result = service.fetchAndSave(loc, List.of(OpenWeatherParameters.CURRENT), 1);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getCurrentWeather()).isNull();
      assertThat(result.getDailyWeather()).isNull();
      assertThat(result.getHourlyWeather()).isNull();
    }
  }
}
