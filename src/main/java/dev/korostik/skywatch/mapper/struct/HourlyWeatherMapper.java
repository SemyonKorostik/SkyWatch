package dev.korostik.skywatch.mapper.struct;

import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HourlyWeatherMapper {

  @Mapping(target = "id.locationId", ignore = true)
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

  @Mapping(target = "time", expression = "java(mapDate(hourlyWeather.getId().getTime(), hourlyWeather.getLocation()))")
  @Mapping(target = "relativeHumidity", source = "humidity")
  @Mapping(target = "surfacePressure", source = "pressure")
  @Mapping(target = "apparentTemperature", ignore = true)
  @Mapping(target = "weatherCode", expression = "java(hourlyWeather.getWeatherCondition().getProviderCode())")
  HourlyData toHourlyData(HourlyWeather hourlyWeather);

  List<HourlyData> toHourlyDataList(List<HourlyWeather> hourlyWeather);

  default LocalDateTime mapDate(Instant date, Location location) {
    return date == null || location == null ? null
        : date.atZone(location.getTimeZoneId().normalized()).toLocalDateTime();
  }

}
