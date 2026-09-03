package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.LocationService;
import dev.korostik.skywatch.service.UserService;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class UserInterceptor implements UpdateInterceptor {
    private final UserService userService;
    private final LocationService locationService;
    private final KeyboardMenu keyboardMenu;

    @Override
    public Update intercept(Update update) {
        /*if(update.message().location() != null) {
            if (userService.existsByChatId(update.message().chat().id())) {
                User user = userService.getByChatId(update.message().chat().id());
                user.setLocation(locationService.getLocation(
                        update.message().location().latitude(),
                        update.message().location().longitude(),
                    Language.valueOf(update.message().from().languageCode())));
                userService.save(user);
            }
        }*/
        return update;
    }
}
