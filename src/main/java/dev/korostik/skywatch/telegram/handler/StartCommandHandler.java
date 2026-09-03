package dev.korostik.skywatch.telegram.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.service.UserService;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor executor;
    private final UserService userService;
    private final KeyboardMenu keyboardMenu;

    private static final Set<String> COMMANDS = Set.of("/start");

    @Override
    public void handle(Update update) {
        if (!userService.existsByChatId(update.message().chat().id())) {
            User user = userService.save(User.builder()
                .chatId(update.message().chat().id())
                .login(update.message().from().firstName())
                .language(update.message().from().languageCode())
                .build());
            SendMessage sendMessage = new SendMessage(user.getChatId(),
                "Hello, " + update.message().from().firstName() + "\nPlease, allow access to location data");
            sendMessage.setReplyMarkup(keyboardMenu.getLocationReplyMarkup());
            executor.execute(sendMessage);
        } else {
            executor.execute(
                new SendMessage(Updates.chatId(update),
                    update.message().from().firstName() + ", we have already started ")
            );
        }
    }

    @Override
    public Set<String> commands() {
        return COMMANDS;
    }
}
