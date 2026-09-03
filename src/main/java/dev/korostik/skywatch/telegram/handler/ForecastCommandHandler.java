package dev.korostik.skywatch.telegram.handler;

import static dev.korostik.skywatch.telegram.button.ButtonTypes.DAILY_FORECAST;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForecastCommandHandler implements CommandUpdateHandler {

  private final TelegramBotExecutor executor;

  private static final Set<String> COMMANDS = Arrays.stream(ButtonTypes.values())
      .filter(x -> x.getType().equals("forecast"))
      .map(ButtonTypes::getName)
      .collect(Collectors.toSet());

  @Override
  public void handle(Update update) {
    switch (ButtonTypes.valueOf(update.message().text())) {
      case DAILY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), """
                🌡️ Current: 19°C (feels like 17°C)
                ☁️ Conditions: partly cloudy
                💧 Humidity: 65%
                💨 Wind: 5.2 m/s
                🌅 Sunrise: 06:23
                🌇 Sunset: 20:15
                """));
        break;
      case WEEKLY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), """
                Monday (15/04): ☁️ 19°C, scattered clouds
                Tuesday (16/04): 🌧️ 16°C, light rain
                Wednesday (17/04): ☀️ 22°C, clear sky
                Thursday (18/04): 🌤️ 20°C, partly cloudy
                Friday (19/04): 🌧️ 17°C, moderate rain
                """));
        break;
      case HOURLY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), """
                🌤️ Hourly Forecast - London
                🕐 09:00: 18°C, clear sky
                🕐 12:00: 21°C, few clouds
                🕐 15:00: 22°C, scattered clouds
                🕐 18:00: 20°C, light rain
                🕐 21:00: 17°C, light rain
                🕐 00:00: 15°C, overcast
                🕐 03:00: 14°C, overcast
                🕐 06:00: 13°C, light rain
                """));
        break;
    }
  }

  @Override
  public Set<String> commands() {
    return COMMANDS;
  }

}
