package dev.korostik.skywatch.telegram.handler.message.buttons;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.service.ForecastService;
import dev.korostik.skywatch.service.UserService;
import dev.korostik.skywatch.service.dto.WeatherSummary;
import dev.korostik.skywatch.service.dto.WeatherSummaryDto;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HourlyForecastButtonHandler implements ButtonHandler {

  private final ForecastService forecastService;
  private final UserService userService;

  private static final Integer NUMBER_OF_DAYS = 1;

  @Override
  public void handle(Update update) {
    User user = userService.getByChatId(update.message().chat().id());
    ForecastResponse response = forecastService.fetchForecast(user.getLocation(),
        List.of(OpenWeatherParameters.HOURLY), NUMBER_OF_DAYS);
    WeatherSummary weatherSummary = forecastService.mapForecast(response, user.getLocation());
    WeatherSummaryDto weatherSummaryDto = forecastService.saveForecast(weatherSummary);
    forecastService.sendForecast(update.message().chat().id(), weatherSummaryDto);
  }

  @Override
  public ButtonTypes getType() {
    return ButtonTypes.HOURLY_FORECAST;
  }
}
