package dev.korostik.skywatch.service.dto;

import dev.korostik.skywatch.entity.CurrentWeather;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherSummary {

  CurrentWeather currentWeather;
  List<DailyWeather> dailyWeather;
  List<HourlyWeather> hourlyWeather;

}
