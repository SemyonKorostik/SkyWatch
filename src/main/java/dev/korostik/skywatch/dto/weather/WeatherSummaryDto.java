package dev.korostik.skywatch.dto.weather;

import dev.korostik.skywatch.entities.CurrentWeather;
import dev.korostik.skywatch.entities.DailyWeather;
import dev.korostik.skywatch.entities.HourlyWeather;
import java.time.Instant;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class WeatherSummaryDto {

  private CurrentWeather currentWeather;
  private Map<Instant, DailyWeather> dailyWeather;
  private Map<Instant, HourlyWeather> hourlyWeather;

}
