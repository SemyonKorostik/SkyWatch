package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.LocationService;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LocationInterceptor implements UpdateInterceptor {

  private final LocationService locationService;

  @Override
  public Update intercept(Update update) {
    if (update.message().location() != null) {
      locationService.sendLocation(update.message().chat().id(),
          Double.valueOf(update.message().location().latitude()),
          Double.valueOf(update.message().location().longitude()),
          Language.valueOf(update.message().from().languageCode()));
      return null;
    }
    return update;
  }
}
