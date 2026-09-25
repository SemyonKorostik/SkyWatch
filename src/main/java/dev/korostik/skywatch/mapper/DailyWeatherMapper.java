package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {HourlyWeatherMapper.class})
public abstract class DailyWeatherMapper {

  @Mapping(target = "date", source = "time")
  @Mapping(target = "temperatureMax", source = "temperature2mMax")
  @Mapping(target = "temperatureMin", source = "temperature2mMin")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(dto.weatherCode()))")
  @Mapping(target = "location", source = "location")
  protected abstract DailyWeather toEntityInternal(DailyData dto, Location location,
      Map<String, WeatherCondition> weatherConditions);


  public List<DailyWeather> toEntities(ForecastResponse dto, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    return dto.daily().stream()
        .map(x -> toEntityInternal(x, location, weatherConditions))
        .toList();
  }
}
