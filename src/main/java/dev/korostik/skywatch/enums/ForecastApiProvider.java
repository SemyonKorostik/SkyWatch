package dev.korostik.skywatch.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ForecastApiProvider {
  OPEN_METEO("open-meteo");
  private final String name;
}
