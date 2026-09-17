package dev.korostik.skywatch.telegram.button;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ButtonTypes {
  SHARE_LOCATION("Share Location", "location"),
  ONE_DAY_FORECAST("1-day Forecast", "forecast"),
  HOURLY_FORECAST("Hourly Forecast", "forecast"),
  CURRENT_FORECAST("Current Forecast", "forecast");
  private final String name;
  private final String type;

  public static Optional<ButtonTypes> from(String name) {
    return Arrays.stream(values()).filter(t -> t.name.equals(name)).findFirst();
  }
}
