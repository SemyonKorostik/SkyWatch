package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.CurrentData;
import dev.korostik.skywatch.entity.CurrentWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrentWeatherMapper {

  @Mapping(target = "temperature", source = "temperature2m")
  @Mapping(target = "windSpeed", source = "windSpeed10m")
  @Mapping(target = "windDirection", source = "windDirection10m")
  @Mapping(target = "humidity", source = "relativeHumidity2m")
  @Mapping(target = "pressure", source = "surfacePressure")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(hourlyData.weatherCode()))")
  CurrentWeather toEntity(CurrentData currentData, Map<String, WeatherCondition> weatherConditions);

}