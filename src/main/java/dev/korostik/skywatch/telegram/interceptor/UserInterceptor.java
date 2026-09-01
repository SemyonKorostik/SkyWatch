package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.repository.UserRepository;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class UserInterceptor implements UpdateInterceptor {
    private final UserRepository userRepository;

    @Override
    public Update intercept(Update update) {
        return update;
    }
}
