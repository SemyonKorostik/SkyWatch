package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
@RequiredArgsConstructor
@Component
public class DailyWeatherMapper {

  public List<DailyWeather> toEntity(ForecastResponse dto, Location location,
      Map<String, WeatherCondition> weatherConditions) {
    return IntStream.range(0, dto.daily().time().size())
        .mapToObj(i -> DailyWeather.builder()
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
