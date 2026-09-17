package dev.korostik.skywatch.telegram.handler.message.buttons;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.service.ForecastService;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HourlyForecastButtonHandler implements ButtonHandler {

  private final ForecastService forecastService;


  @Override
  public void handle(Update update) {
    forecastService.sendHourlyForecast(update.message().chat().id());
  }

  @Override
  public ButtonTypes getType() {
    return ButtonTypes.HOURLY_FORECAST;
  }
}
