package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.HourlyWeatherId;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HourlyWeatherMapper {

  public List<HourlyWeather> toEntity(ForecastResponse dto, Location location, DailyWeather dailyWeather,
      Map<String, WeatherCondition> weatherConditions) {
    return IntStream.range(0, dto.hourly().time().size())
        .mapToObj(i -> HourlyWeather.builder()
            .id(new HourlyWeatherId(dto.hourly().time().get(i), dailyWeather))
            .createdAt(Instant.ofEpochMilli(dto.generationTimeMs()))
            .date(dto.daily().time().get(i))
            .temperatureMax(dto.daily().temperature2mMax().get(i))
            .temperatureMin(dto.daily().temperature2mMin().get(i))
            .weatherCondition(weatherConditions.get(dto.daily().weatherCode().get(i)))
            .location(location)
            .build())
        .collect(Collectors.toList());
  }
}
