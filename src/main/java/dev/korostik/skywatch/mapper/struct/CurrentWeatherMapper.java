package dev.korostik.skywatch.mapper.struct;

import dev.korostik.skywatch.dto.weather.external.CurrentDataDto;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
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
  CurrentWeather toEntity(CurrentDataDto currentData, @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

  @Mapping(target = "time", ignore = true)
  @Mapping(target = "interval", ignore = true)
  @Mapping(target = "apparentTemperature", ignore = true)
  @Mapping(target = "relativeHumidity", source = "humidity")
  @Mapping(target = "surfacePressure", source = "pressure")
  @Mapping(target = "weatherCode", expression = "java(currentWeather.getWeatherCondition().getProviderCode())")
  CurrentDataDto toDto(CurrentWeather currentWeather);
}