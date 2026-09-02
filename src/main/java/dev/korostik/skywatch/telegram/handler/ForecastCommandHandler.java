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
        executor.execute(new SendMessage(Updates.chatId(update), "+18C Rain cats and dogs"));
        break;
      case WEEKLY_FORECAST:
        break;
      case HOURLY_FORECAST:
        break;
    }
  }

  @Override
  public Set<String> commands() {
    return COMMANDS;
  }

}
