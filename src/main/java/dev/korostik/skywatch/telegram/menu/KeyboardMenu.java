package dev.korostik.skywatch.telegram.menu;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class KeyboardMenu {

  private KeyboardButton locationButton;
  private KeyboardButton dailyForecastButton;
  private KeyboardButton hourlyForecastButton;
  private KeyboardButton weeklyForecastButton;

  @PostConstruct
  public void init() {
    locationButton = new KeyboardButton("Share location");
    dailyForecastButton = new KeyboardButton("Daily Forecast");
    hourlyForecastButton = new KeyboardButton("Hourly Forecast");
    weeklyForecastButton = new KeyboardButton("Weekly Forecast");
  }

  public ReplyKeyboardMarkup getLocationMenu() {
    return new ReplyKeyboardMarkup(locationButton);
  }

  public ReplyKeyboardMarkup getFullMenu() {
    return new ReplyKeyboardMarkup(dailyForecastButton, hourlyForecastButton, weeklyForecastButton,
        locationButton);
  }
}
