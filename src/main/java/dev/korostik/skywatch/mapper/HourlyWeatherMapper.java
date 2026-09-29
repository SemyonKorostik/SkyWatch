package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {HourlyWeatherIdMapper.class})
public abstract class HourlyWeatherMapper {

  @Autowired
  private HourlyWeatherIdMapper idMapper;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "temperature", source = "hourlyData.temperature")
  @Mapping(target = "precipitation", source = "hourlyData.precipitation")
  @Mapping(target = "precipitationProbability", source = "hourlyData.precipitationProbability")
  @Mapping(target = "windSpeed", source = "hourlyData.windSpeed")
  @Mapping(target = "windDirection", source = "hourlyData.windDirection")
  @Mapping(target = "humidity", source = "hourlyData.relativeHumidity")
  @Mapping(target = "pressure", source = "hourlyData.surfacePressure")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(hourlyData.weatherCode()))")
  protected abstract HourlyWeather toEntityInternal(HourlyData hourlyData, Location location,
      Map<String, WeatherCondition> weatherConditions);

  public HourlyWeather toEntity(
      HourlyData hourlyData, Location location,
      Map<String, WeatherCondition> weatherConditions) {

    HourlyWeather entity = toEntityInternal(hourlyData, location,
        weatherConditions);
    entity.setId(idMapper.toEntity(hourlyData, location));
    return entity;
  }

  public List<HourlyWeather> toEntities(List<HourlyData> hourlyDataList, Location location,
      Map<String, WeatherCondition> weatherConditions) {
    return hourlyDataList.stream()
        .map(x -> toEntity(x, location, weatherConditions))
        .collect(Collectors.toList());
  }
}
