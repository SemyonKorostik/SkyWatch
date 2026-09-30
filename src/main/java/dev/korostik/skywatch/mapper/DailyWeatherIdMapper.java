package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.entity.DailyWeatherId;
import dev.korostik.skywatch.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DailyWeatherIdMapper {

  @Mapping(target = "date", expression = "java(dailyData.time().atStartOfDay(location.getZoneId()).toInstant())")
  @Mapping(target = "locationId", source = "location.id")
  DailyWeatherId toEntity(DailyData dailyData, Location location);
}
