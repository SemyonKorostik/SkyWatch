package dev.korostik.skywatch.service.dto;

import dev.korostik.skywatch.entity.CurrentWeather;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import java.time.Instant;
import java.time.OffsetDateTime;
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
