package dev.korostik.skywatch.service.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import dev.korostik.skywatch.repository.CurrentWeatherRepository;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import dev.korostik.skywatch.repository.HourlyWeatherRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WeatherPersistenceServiceImplUnitTest {

  private CurrentWeatherRepository currentRepo;
  private DailyWeatherRepository dailyRepo;
  private HourlyWeatherRepository hourlyRepo;
  private WeatherPersistenceServiceImpl service;

  @BeforeEach
  void setUp() {
    currentRepo = Mockito.mock(CurrentWeatherRepository.class);
    dailyRepo = Mockito.mock(DailyWeatherRepository.class);
    hourlyRepo = Mockito.mock(HourlyWeatherRepository.class);
    service = new WeatherPersistenceServiceImpl(currentRepo, dailyRepo, hourlyRepo);
  }

  @Test
  @DisplayName("Should save all weather parts")
  void save_withAllFields_savesAll() {
    // given
    CurrentWeather current = CurrentWeather.builder().build();
    DailyWeather daily = DailyWeather.builder().build();
    HourlyWeather hourly = HourlyWeather.builder().build();

    when(currentRepo.save(current)).thenReturn(current);
    when(dailyRepo.saveAll(List.of(daily))).thenReturn(List.of(daily));
    when(hourlyRepo.saveAll(List.of(hourly))).thenReturn(List.of(hourly));

    WeatherSummary input = WeatherSummary.builder()
        .currentWeather(current)
        .dailyWeather(List.of(daily))
        .hourlyWeather(List.of(hourly))
        .build();

    // when
    WeatherSummary result = service.save(input);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getCurrentWeather()).isEqualTo(current);
    assertThat(result.getDailyWeather()).isEqualTo(List.of(daily));
    assertThat(result.getHourlyWeather()).isEqualTo(List.of(hourly));
    verify(currentRepo).save(current);
    verify(dailyRepo).saveAll(List.of(daily));
    verify(hourlyRepo).saveAll(List.of(hourly));
  }

  @Test
  @DisplayName("Should return null fields when input is null")
  void save_withNullFields_returnsNullFields() {
    // given
    WeatherSummary input = WeatherSummary.builder()
        .currentWeather(null)
        .dailyWeather(null)
        .hourlyWeather(null)
        .build();

    // when
    WeatherSummary result = service.save(input);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getCurrentWeather()).isNull();
    assertThat(result.getDailyWeather()).isNull();
    assertThat(result.getHourlyWeather()).isNull();
  }
}
