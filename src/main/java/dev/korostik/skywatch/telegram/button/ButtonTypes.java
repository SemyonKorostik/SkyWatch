package dev.korostik.skywatch.telegram.button;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ButtonTypes {
  SHARE_LOCATION("Share location", "location"),
  DAILY_FORECAST("Daily Forecast", "forecast"),
  HOURLY_FORECAST("Hourly Forecast", "forecast"),
  WEEKLY_FORECAST("Weekly Forecast", "forecast");
  private final String name;
  private final String type;

  public static ButtonTypes from(String name) {
    return Arrays.stream(values()).filter(t -> t.name.equals(name)).findFirst().orElse(null);
  }
}
