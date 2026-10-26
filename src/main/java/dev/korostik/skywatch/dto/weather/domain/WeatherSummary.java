package dev.korostik.skywatch.dto.weather.domain;

import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeatherSummary {

  CurrentWeather currentWeather;
  List<DailyWeather> dailyWeather;
  List<HourlyWeather> hourlyWeather;
  LocationName locationName;

}
