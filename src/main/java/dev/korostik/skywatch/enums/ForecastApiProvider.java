package dev.korostik.skywatch.enums;

import jakarta.persistence.EnumeratedValue;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ForecastApiProvider {
  OPEN_METEO("open-meteo");
  @EnumeratedValue
  private final String name;

  public static Optional<ForecastApiProvider> from(String name) {
    return Arrays.stream(values()).filter(t -> t.name.equals(name)).findFirst();
  }
}
