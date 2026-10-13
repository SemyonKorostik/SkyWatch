package dev.korostik.skywatch.dto.weather;

import dev.korostik.skywatch.entities.CurrentWeather;
import dev.korostik.skywatch.entities.DailyWeather;
import dev.korostik.skywatch.entities.HourlyWeather;
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
