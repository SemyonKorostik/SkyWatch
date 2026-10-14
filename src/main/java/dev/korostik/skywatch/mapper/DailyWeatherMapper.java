package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.transit.DailyData;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ZoneId.class)
public interface DailyWeatherMapper {

  @Mapping(target = "id.date", expression = "java(mapDate(dailyData.date(), location))")
  @Mapping(target = "location", expression = "java(location)")
  @Mapping(target = "windSpeed", ignore = true)
  @Mapping(target = "windDirection", ignore = true)
  @Mapping(target = "humidity", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "weatherCondition", expression = "java(weatherConditions.get(dailyData.weatherCode()))")
  DailyWeather toEntity(DailyData dailyData,
      @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

  List<DailyWeather> toEntities(List<DailyData> dto,
      @Context Location location,
      @Context Map<String, WeatherCondition> weatherConditions);

  default Instant mapDate(LocalDate date, Location location) {
    return date == null ? null
        : date.atStartOfDay(location.getTimeZoneId().normalized()).toInstant();
  }

  @Mapping(target = "date", expression = "java(mapDate(dailyWeather.getId().getDate()))")
  @Mapping(target = "weatherCode", expression = "java(dailyWeather.getWeatherCondition().getProviderCode())")
  DailyData toDailyData(DailyWeather dailyWeather);

  List<DailyData> toDailyDataList(List<DailyWeather> dailyWeather);

  default LocalDate mapDate(Instant date) {
    return date == null ? null : LocalDate.from(date);
  }
}
