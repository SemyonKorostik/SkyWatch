package dev.korostik.skywatch.telegram.menu;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import dev.korostik.skywatch.telegram.button.ButtonTypes;
import org.springframework.stereotype.Component;

//Фабрика
@Component
public class KeyboardMenu {

  public ReplyKeyboardMarkup getLocationReplyMarkup() {
    KeyboardButton locationButton = new KeyboardButton(
        ButtonTypes.SHARE_LOCATION.getName()).requestLocation(true);
    return new ReplyKeyboardMarkup(locationButton).resizeKeyboard(true);
  }

  public ReplyKeyboardMarkup getForecastReplyMarkup() {
    KeyboardButton oneDayForecastButton = new KeyboardButton(
        ButtonTypes.ONE_DAY_FORECAST.getName());
    KeyboardButton hourlyForecastButton = new KeyboardButton(ButtonTypes.HOURLY_FORECAST.getName());
    KeyboardButton currentForecastButton = new KeyboardButton(
        ButtonTypes.CURRENT_FORECAST.getName());
    KeyboardButton locationButton = new KeyboardButton(
        ButtonTypes.SHARE_LOCATION.getName()).requestLocation(true);
    return new ReplyKeyboardMarkup(
        new KeyboardButton[]{currentForecastButton, hourlyForecastButton},
        new KeyboardButton[]{oneDayForecastButton, locationButton}
    ).resizeKeyboard(true);
  }
}
