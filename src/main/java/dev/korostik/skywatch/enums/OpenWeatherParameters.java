package dev.korostik.skywatch.enums;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpenWeatherParameters {
  DAILY(List.of("temperature_2m_max", "temperature_2m_min", "weather_code")),
  HOURLY(List.of("temperature_2m", "relative_humidity_2m", "weather_code", "apparent_temperature",
      "precipitation", "precipitation_probability")),
  CURRENT(List.of("temperature_2m", "relative_humidity_2m", "weather_code", "apparent_temperature",
      "wind_speed_10m", "wind_direction_10m", "surface_pressure"));
  private final List<String> parameters;
}
