package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {DailyWeatherIdMapper.class})
public abstract class DailyWeatherMapper {

  protected abstract DailyWeatherIdMapper idMapper();

  @Mapping(target = "temperatureMax", source = "temperature2mMax")
  @Mapping(target = "temperatureMin", source = "temperature2mMin")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(dto.weatherCode()))")
  @Mapping(target = "location", source = "location")
  protected abstract DailyWeather toEntityInternal(DailyData dto, Location location,
      Map<String, WeatherCondition> weatherConditions);

  public DailyWeather toEntity(
      DailyData dailyData, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    DailyWeather entity = toEntityInternal(dailyData, location,
        weatherConditions);
    entity.setId(idMapper().toEntity(dailyData, location));
    return entity;
  }

  public List<DailyWeather> toEntities(List<DailyData> dtoList, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    return dtoList.stream()
        .map(x -> toEntity(x, location, weatherConditions))
        .toList();
  }
}
