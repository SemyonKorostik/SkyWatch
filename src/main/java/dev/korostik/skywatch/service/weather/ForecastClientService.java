package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.enums.Language;

public interface ForecastClientService {

  void sendCurrentWeather(Long chatId, Long locationId, Language language);

  void sendHourlyForecast(Long chatId, Long locationId, Language language);

  void sendDailyForecast(Long chatId, Long locationId, Language language);

  void sendWeeklyForecast(Long chatId, Long locationId, Language language);


}
