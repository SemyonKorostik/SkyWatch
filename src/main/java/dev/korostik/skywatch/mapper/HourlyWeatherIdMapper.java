package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.HourlyWeatherId;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HourlyWeatherIdMapper {

  @Mapping(target = "time", expression = "java(hourlyData.time().atZone(zoneOffset).toOffsetDateTime())")
  @Mapping(target = "dailyWeatherId", source = "dailyWeather.id")
  HourlyWeatherId toEntity(HourlyData hourlyData, ZoneOffset zoneOffset, DailyWeather dailyWeather);
}
