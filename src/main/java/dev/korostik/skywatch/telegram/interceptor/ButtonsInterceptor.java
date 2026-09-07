package dev.korostik.skywatch.telegram.interceptor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.service.ForecastService;
import dev.korostik.skywatch.service.LocationService;
import dev.korostik.skywatch.service.UserService;
import dev.korostik.skywatch.service.WeatherConditionService;
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
  private final ForecastService forecastService;
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
    switch (ButtonTypes.from(update.message().text())) {
      case DAILY_FORECAST:
        Iterable<DailyWeather> dailyForecast = forecastService.getDailyForecast(userService.getByChatId(update.message().chat().id()).getLocation(), 1);
        DailyWeather next = dailyForecast.iterator().next();
        executor.execute(new SendMessage(Updates.chatId(update), next.toString()));
        break;
      case WEEKLY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), """
                Monday (15/04): ☁️ 19°C, scattered clouds
                Tuesday (16/04): 🌧️ 16°C, light rain
                Wednesday (17/04): ☀️ 22°C, clear sky
                Thursday (18/04): 🌤️ 20°C, partly cloudy
                Friday (19/04): 🌧️ 17°C, moderate rain
                """));
        break;
      case HOURLY_FORECAST:
        executor.execute(new SendMessage(Updates.chatId(update), """
                🌤️ Hourly Forecast - London
                🕐 09:00: 18°C, clear sky
                🕐 12:00: 21°C, few clouds
                🕐 15:00: 22°C, scattered clouds
                🕐 18:00: 20°C, light rain
                🕐 21:00: 17°C, light rain
                🕐 00:00: 15°C, overcast
                🕐 03:00: 14°C, overcast
                🕐 06:00: 13°C, light rain
                """));
        break;
      case SHARE_LOCATION:
        if (update.message().location() != null) {
          if (userService.existsByChatId(update.message().chat().id())) {
            User user = userService.getByChatId(update.message().chat().id());
              Long locationId = user.getLocation().getId();
              user.setLocation(locationService.getLocation(
                update.message().location().latitude(),
                update.message().location().longitude(),
                Language.valueOf(update.message().from().languageCode())));
              user.getLocation().setId(locationId);
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
