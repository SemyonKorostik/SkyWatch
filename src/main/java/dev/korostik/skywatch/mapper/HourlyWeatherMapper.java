package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entities.HourlyWeather;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.WeatherCondition;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HourlyWeatherMapper {

  @Mapping(target = "id.time", expression = "java(mapDate(hourlyData.time(), location))")
  @Mapping(target = "location", expression = "java(location)")
  @Mapping(target = "humidity", source = "relativeHumidity")
  @Mapping(target = "pressure", source = "surfacePressure")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(hourlyData.weatherCode()))")
  HourlyWeather toEntity(HourlyData hourlyData, @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

  List<HourlyWeather> toEntities(List<HourlyData> hourlyData, @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

  default Instant mapDate(LocalDateTime dateTime, Location location) {
    return dateTime == null ? null
        : dateTime.atZone(location.getTimeZoneId().normalized()).toInstant();
  }

}
