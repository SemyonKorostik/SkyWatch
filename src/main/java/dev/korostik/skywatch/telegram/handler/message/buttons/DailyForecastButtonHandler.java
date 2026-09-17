package dev.korostik.skywatch.telegram.handler.message.buttons;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.service.ForecastService;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyForecastButtonHandler implements ButtonHandler {

  private final ForecastService forecastService;

  @Override
  public void handle(Update update) {
    forecastService.sendDailyForecast(update.message().chat().id(), 1);
  }

  @Override
  public ButtonTypes getType() {
    return ButtonTypes.ONE_DAY_FORECAST;
  }
}
