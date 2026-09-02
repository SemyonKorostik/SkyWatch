package dev.korostik.skywatch.telegram.button;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ButtonTypes {
  SHARE_LOCATION("Share location", "location"),
  DAILY_FORECAST("Daily forecast", "forecast"),
  HOURLY_FORECAST("Hourly Forecast", "forecast"),
  WEEKLY_FORECAST("Weekly Forecast", "forecast");
  private final String name;
  private final String type;
}
