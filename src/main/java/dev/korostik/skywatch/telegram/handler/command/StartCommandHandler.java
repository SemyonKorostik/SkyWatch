package dev.korostik.skywatch.telegram.handler.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.entities.user.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.user.UserServiceImpl;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandUpdateHandler {

  private final TelegramBotExecutor executor;
  private final UserServiceImpl userService;
  private final KeyboardMenu keyboardMenu;

  private static final Set<String> COMMANDS = Set.of("/start");

  @Override
  public void handle(Update update) {
    if (!userService.existsByChatId(update.message().chat().id())) {
      User user = userService.save(User.builder()
          .chatId(update.message().chat().id())
          .login(update.message().from().firstName())
          .language(Language.from(update.message().from().languageCode()).orElseThrow())
          .build());
      SendMessage sendMessage = new SendMessage(user.getChatId(),
          "Hello, " + update.message().from().firstName()
              + "\nPlease, allow access to location value");
      sendMessage.setReplyMarkup(keyboardMenu.getLocationReplyMarkup());
      executor.execute(sendMessage);
    } else {
      SendMessage sendMessage = new SendMessage(Updates.chatId(update),
          update.message().from().firstName() + ", we have already started ");
      sendMessage.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
      executor.execute(sendMessage);

    }
  }

  @Override
  public Set<String> commands() {
    return COMMANDS;
  }
}
