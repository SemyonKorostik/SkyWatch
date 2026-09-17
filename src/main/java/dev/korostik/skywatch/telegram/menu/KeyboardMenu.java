package dev.korostik.skywatch.telegram.menu;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

//Фабрика
@Component
public class KeyboardMenu {

  private KeyboardButton locationButton;
  private KeyboardButton oneDayForecastButton;
  private KeyboardButton hourlyForecastButton;
  private KeyboardButton currentForecastButton;
  @Getter
  private ReplyKeyboardMarkup locationReplyMarkup;
  @Getter
  private ReplyKeyboardMarkup forecastReplyMarkup;

  @PostConstruct
  public void init() {
    locationButton = new KeyboardButton(ButtonTypes.SHARE_LOCATION.getName()).requestLocation(true);
    oneDayForecastButton = new KeyboardButton(ButtonTypes.ONE_DAY_FORECAST.getName());
    hourlyForecastButton = new KeyboardButton(ButtonTypes.HOURLY_FORECAST.getName());
    currentForecastButton = new KeyboardButton(ButtonTypes.CURRENT_FORECAST.getName());
    locationReplyMarkup = new ReplyKeyboardMarkup(locationButton);
    forecastReplyMarkup = new ReplyKeyboardMarkup(currentForecastButton, hourlyForecastButton,
        oneDayForecastButton, locationButton).resizeKeyboard(true);
  }
}
