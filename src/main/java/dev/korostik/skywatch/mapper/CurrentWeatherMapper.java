package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.CurrentData;
import dev.korostik.skywatch.entity.CurrentWeather;
import dev.korostik.skywatch.entity.Location;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrentWeatherMapper {

  @Mapping(target = "locationId", source = "location.id")
  @Mapping(target = "temperature", source = "currentData.temperature")
  @Mapping(target = "windSpeed", source = "currentData.windSpeed")
  @Mapping(target = "windDirection", source = "currentData.windDirection")
  @Mapping(target = "humidity", source = "currentData.relativeHumidity")
  @Mapping(target = "pressure", source = "currentData.surfacePressure")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(currentData.weatherCode()))")
  CurrentWeather toEntity(CurrentData currentData, Location location, Map<String, WeatherCondition> weatherConditions);

}