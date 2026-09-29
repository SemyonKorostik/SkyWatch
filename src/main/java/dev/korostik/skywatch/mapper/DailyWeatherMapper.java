package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {DailyWeatherIdMapper.class})
public abstract class DailyWeatherMapper {

  @Autowired
  private DailyWeatherIdMapper idMapper;

  @Mapping(target = "temperatureMax", source = "dto.temperatureMax")
  @Mapping(target = "temperatureMin", source = "dto.temperatureMin")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(dto.weatherCode()))")
  @Mapping(target = "location", source = ".")
  protected abstract DailyWeather toEntityInternal(DailyData dto, Location location,
      Map<String, WeatherCondition> weatherConditions);

  public DailyWeather toEntity(
      DailyData dailyData, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    DailyWeather entity = toEntityInternal(dailyData, location,
        weatherConditions);
    entity.setId(idMapper.toEntity(dailyData, location));
    return entity;
  }

  public List<DailyWeather> toEntities(List<DailyData> dtoList, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    return dtoList.stream()
        .map(x -> toEntity(x, location, weatherConditions))
        .toList();
  }
}
