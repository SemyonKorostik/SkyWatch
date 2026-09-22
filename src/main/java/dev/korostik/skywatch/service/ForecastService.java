package dev.korostik.skywatch.service;

import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.client.OpenWeatherApiClientProxy;
import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.mapper.DailyWeatherMapper;
import dev.korostik.skywatch.repository.DailyWeatherRepository;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static dev.korostik.skywatch.enums.OpenWeatherParameters.*;

@Service
@RequiredArgsConstructor
public class ForecastService {
//использовать Прокси для получения запроса от разных поставщиков
  private final OpenWeatherApiClientProxy openWeatherApiClientProxy;
  private final DailyWeatherMapper weatherMapper;
  private final DailyWeatherRepository dailyWeatherRepository;
  private final Map<String, WeatherCondition> weatherConditionOpenMeteoDictionary;
  private final UserService userService;
  private final TelegramBotExecutor executor;

  @Transactional
  public List<DailyWeather> fetchAndSaveDailyForecast(Location location,
      OpenWeatherParameters parameters, int numberOfDays) {
    return dailyWeatherRepository.saveAll(weatherMapper.toEntity(
        openWeatherApiClientProxy.getForecast(
            ForecastRequest.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .forecastDays(numberOfDays)
                .timezone(location.getTimeZoneId())
                .current(CURRENT == parameters ? parameters.getParameters() : null)
                .hourly(HOURLY == parameters ? parameters.getParameters() : null)
                .daily(DAILY == parameters ? parameters.getParameters() : null)
                .build()
        ), location, weatherConditionOpenMeteoDictionary));
  }

  @Transactional
  public void sendDailyForecast(Long chatId, int numberOfDays) {
    Location location = userService.getByChatId(chatId).getLocation();
    List<DailyWeather> forecast = fetchAndSaveDailyForecast(location, OpenWeatherParameters.DAILY,
        numberOfDays);
    executor.execute(new SendMessage(chatId, forecast.toString()));
  }

  @Transactional
  public void sendCurrentForecast(Long chatId) {
    Location location = userService.getByChatId(chatId).getLocation();
    List<DailyWeather> forecast = fetchAndSaveDailyForecast(location, CURRENT,
        1);
    executor.execute(new SendMessage(chatId, forecast.toString()));
  }

  @Transactional
  public void sendHourlyForecast(Long chatId) {
    Location location = userService.getByChatId(chatId).getLocation();
    List<DailyWeather> forecast = fetchAndSaveDailyForecast(location, OpenWeatherParameters.HOURLY,
        1);
    executor.execute(new SendMessage(chatId, forecast.toString()));
  }
}
