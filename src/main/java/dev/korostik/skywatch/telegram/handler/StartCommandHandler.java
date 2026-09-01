package dev.korostik.skywatch.telegram.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public void handle(Update update) {
        if (!userRepository.existsUserByChatId(update.message().chat().id()))
            userRepository.save(User.builder()
                    .chatId(update.message().chat().id())
                    .login(update.message().from().firstName())
                    .language(update.message().from().languageCode())
                    .build());
        executor.execute(
                new SendMessage(Updates.chatId(update), "Hello " + update.message().from().firstName())
        );
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start");
    }
}
