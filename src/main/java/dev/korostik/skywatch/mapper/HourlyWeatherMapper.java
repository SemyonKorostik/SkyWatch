package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.WeatherCondition;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HourlyWeatherIdMapper.class})
public abstract class HourlyWeatherMapper {

  protected abstract HourlyWeatherIdMapper idMapper();

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "temperature", source = "temperature2m")
  @Mapping(target = "precipitation", source = "precipitation")
  @Mapping(target = "precipitationProbability", source = "precipitationProbability")
  @Mapping(target = "windSpeed", source = "windSpeed10m")
  @Mapping(target = "windDirection", source = "windDirection10m")
  @Mapping(target = "humidity", source = "relativeHumidity2m")
  @Mapping(target = "pressure", source = "surfacePressure")
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(hourlyData.weatherCode()))")
  protected abstract HourlyWeather toEntityInternal(HourlyData hourlyData, ZoneOffset zoneOffset,
      DailyWeather dailyWeather, Map<String, WeatherCondition> weatherConditions);

  public HourlyWeather toEntity(
      HourlyData hourlyData, ZoneOffset zoneOffset, DailyWeather dailyWeather,
      Map<String, WeatherCondition> weatherConditions) {

    HourlyWeather entity = toEntityInternal(hourlyData, zoneOffset, dailyWeather,
        weatherConditions);
    entity.setId(idMapper().toEntity(hourlyData, zoneOffset, dailyWeather));
    return entity;
  }

  public List<HourlyWeather> toEntities(List<HourlyData> hourlyDataList, ZoneOffset zoneOffset,
      DailyWeather dailyWeather,
      Map<String, WeatherCondition> weatherConditions) {
    return hourlyDataList.stream()
        .map(x -> toEntity(x, zoneOffset, dailyWeather, weatherConditions))
        .collect(Collectors.toList());
  }
}
