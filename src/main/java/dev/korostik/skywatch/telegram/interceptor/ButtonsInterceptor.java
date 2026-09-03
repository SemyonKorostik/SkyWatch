package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.LocationService;
import dev.korostik.skywatch.service.UserService;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import io.ksilisk.telegrambot.core.update.Updates;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ButtonsInterceptor implements UpdateInterceptor {

  private final UserService userService;
  private final LocationService locationService;
  private final TelegramBotExecutor executor;
  private final KeyboardMenu keyboardMenu;

  private static final Set<String> COMMANDS = Arrays.stream(ButtonTypes.values())
      .map(ButtonTypes::getName)
      .collect(Collectors.toSet());

  @Override
  public Update intercept(Update update) {
    if (!COMMANDS.contains(update.message().text())) {
      return update;
    }
    switch (ButtonTypes.valueOf(update.message().text())) {
      case DAILY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), "+18C Rain cats and dogs"));
        break;
      case WEEKLY_FORECAST:
        break;
      case HOURLY_FORECAST:
        break;
      case SHARE_LOCATION:
        if (update.message().location() != null) {
          if (userService.existsByChatId(update.message().chat().id())) {
            User user = userService.getByChatId(update.message().chat().id());
            user.setLocation(locationService.getLocation(
                update.message().location().latitude(),
                update.message().location().longitude(),
                Language.valueOf(update.message().from().languageCode())));
            userService.save(user);
            SendMessage sendMessage = new SendMessage(Updates.chatId(update), "");
            sendMessage.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
            executor.execute(sendMessage);
          } else {
            executor.execute(new SendMessage(Updates.chatId(update), "You are not register. Try /start"));
          }
        } else {
          executor.execute(new SendMessage(Updates.chatId(update), "Location missing, try again"));
        }
        return update;
    }
    return update;
  }
}
