package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.LocationService;
import dev.korostik.skywatch.service.UserService;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LocationInterceptor implements UpdateInterceptor {

  private final UserService userService;
  private final LocationService locationService;

  @Override
  public Update intercept(Update update) {
    if (update.message().location() != null) {
      User user = userService.getByChatId(update.message().chat().id());
      Location location = locationService.getNearest(
              Double.valueOf(update.message().location().latitude()),
              Double.valueOf(update.message().location().longitude()))
          .orElse(locationService.fetch(Double.valueOf(update.message().location().latitude()),
                  Double.valueOf(update.message().location().longitude()),
                  Language.valueOf(update.message().from().languageCode()))
              .orElseThrow());
      locationService.save(location);
      user.setLocation(location);
      userService.save(user);
      locationService.sendLocation(update.message().chat().id(), location);
      return null;
    }
    return update;
  }
}
