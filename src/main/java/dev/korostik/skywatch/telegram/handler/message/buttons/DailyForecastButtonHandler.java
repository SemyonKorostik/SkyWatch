package dev.korostik.skywatch.telegram.handler.message.buttons;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.entities.user.User;
import dev.korostik.skywatch.service.user.UserServiceImpl;
import dev.korostik.skywatch.service.weather.ForecastClientService;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyForecastButtonHandler implements ButtonHandler {

  private final ForecastClientService forecastClientService;
  private final UserServiceImpl userService;

  @Override
  public void handle(Update update) {
    User user = userService.findWithLocationByChatId(update.message().chat().id());
    forecastClientService.sendDailyForecast(user.getChatId(), user.getLocation().getId(),
        user.getLanguage());
  }

  @Override
  public ButtonTypes getType() {
    return ButtonTypes.ONE_DAY_FORECAST;
  }
}
