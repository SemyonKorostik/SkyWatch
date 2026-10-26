package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.user.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.location.LocationClientService;
import dev.korostik.skywatch.service.user.UserServiceImpl;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LocationInterceptor implements UpdateInterceptor {

  private final UserServiceImpl userService;
  private final LocationClientService locationClientService;

  @Override
  public Update intercept(Update update) {
    if (update.message().location() != null) {
      User user = userService.getByChatId(update.message().chat().id());
      LocationName locationName = locationClientService.findOrCreateLocationName(
          Double.valueOf(update.message().location().latitude()),
          Double.valueOf(update.message().location().longitude()),
          Language.from(update.message().from().languageCode()).orElseThrow());
      user.setLocation(locationName.getLocation());
      userService.save(user);
      locationClientService.sendLocation(update.message().chat().id(), locationName);
      return null;
    }
    return update;
  }
}
