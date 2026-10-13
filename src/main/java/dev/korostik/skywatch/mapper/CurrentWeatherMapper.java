package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.CurrentData;
import dev.korostik.skywatch.entities.CurrentWeather;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.WeatherCondition;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrentWeatherMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "location", expression = "java(location)")
  @Mapping(target = "humidity", source = "relativeHumidity")
  @Mapping(target = "pressure", source = "surfacePressure")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(currentData.weatherCode()))")
  CurrentWeather toEntity(CurrentData currentData, @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

}