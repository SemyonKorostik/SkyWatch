package dev.korostik.skywatch.service;

import static dev.korostik.skywatch.enums.OpenWeatherParameters.CURRENT;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.DAILY;
import static dev.korostik.skywatch.enums.OpenWeatherParameters.HOURLY;

import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.client.OpenMeteoApiClientProxy;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.mapper.CurrentWeatherMapper;
import dev.korostik.skywatch.mapper.DailyWeatherMapper;
import dev.korostik.skywatch.mapper.HourlyWeatherMapper;
import dev.korostik.skywatch.repository.CurrentWeatherRepository;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import dev.korostik.skywatch.repository.HourlyWeatherRepository;
import dev.korostik.skywatch.service.dto.WeatherSummary;
import dev.korostik.skywatch.service.dto.WeatherSummaryDto;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForecastService {

  /// TO-DO использовать Прокси для получения запроса от разных поставщиков
  private final OpenMeteoApiClientProxy openMeteoApiClientProxy;

  private final CurrentWeatherMapper currentWeatherMapper;
  private final DailyWeatherMapper dailyWeatherMapper;
  private final HourlyWeatherMapper hourlyWeatherMapper;

  private final CurrentWeatherRepository currentWeatherRepository;
  private final DailyWeatherRepository dailyWeatherRepository;
  private final HourlyWeatherRepository hourlyWeatherRepository;

  private final Map<String, WeatherCondition> weatherConditionOpenMeteoDictionary;
  private final TelegramBotExecutor executor;

  public ForecastResponse fetchForecast(Location location,
      List<OpenWeatherParameters> parameters, int numberOfDays) {
    return openMeteoApiClientProxy.getForecast(
        ForecastRequest.builder()
            .latitude(location.getLatitude())
            .longitude(location.getLongitude())
            .forecastDays(numberOfDays)
            .timezone(location.getTimeZone().getId())
            .current(parameters.contains(CURRENT) ? CURRENT.getParameters() : null)
            .hourly(parameters.contains(HOURLY) ? HOURLY.getParameters() : null)
            .daily(parameters.contains(DAILY) ? DAILY.getParameters() : null)
            .build()
    );
  }

  public WeatherSummary mapForecast(ForecastResponse response, Location location) {
    return WeatherSummary.builder()
        .currentWeather(Optional.ofNullable(response.current())
            .map(x -> currentWeatherMapper.toEntity(x,
                weatherConditionOpenMeteoDictionary))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(response.daily())
            .map(daily -> dailyWeatherMapper.toEntities(daily, location,
                weatherConditionOpenMeteoDictionary))
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(response.hourly())
            .map(hourly -> hourlyWeatherMapper.toEntities(hourly, location,
                weatherConditionOpenMeteoDictionary))
            .orElse(null))
        .build();
  }

  @Transactional
  public WeatherSummaryDto saveForecast(WeatherSummary weatherSummary) {
    return WeatherSummaryDto.builder()
        .currentWeather(Optional.ofNullable(weatherSummary.getCurrentWeather())
            .map(x -> currentWeatherRepository.save(weatherSummary.getCurrentWeather()))
            .orElse(null))
        .dailyWeather(Optional.ofNullable(weatherSummary.getDailyWeather())
            .map(daily -> dailyWeatherRepository.saveAll(weatherSummary.getDailyWeather())
                .stream()
                .collect(Collectors.toUnmodifiableMap(x -> x.getId().getTime(), x -> x)))
            .orElse(null))
        .hourlyWeather(Optional.ofNullable(weatherSummary.getHourlyWeather())
            .map(hourly -> hourlyWeatherRepository.saveAll(weatherSummary.getHourlyWeather())
                .stream()
                .collect(Collectors.toUnmodifiableMap(x -> x.getId().getTime(), x -> x)))
            .orElse(null))
        .build();

  }

  public void sendForecast(Long chatId, WeatherSummaryDto weatherSummaryDto) {
    executor.execute(new SendMessage(chatId, weatherSummaryDto.toString()));
  }

}
