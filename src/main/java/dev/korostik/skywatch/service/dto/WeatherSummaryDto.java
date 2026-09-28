package dev.korostik.skywatch.service.dto;

import dev.korostik.skywatch.entity.CurrentWeather;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class WeatherSummaryDto {
  CurrentWeather currentWeather;
  Map<OffsetDateTime, DailyWeather> dailyWeather;
  Map<OffsetDateTime, HourlyWeather> hourlyWeather;

}
