package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.entity.HourlyWeatherId;
import dev.korostik.skywatch.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HourlyWeatherIdMapper {

  @Mapping(target = "time", expression = "java(hourlyData.time().atZone(location.getZoneId()).toInstant())")
  @Mapping(target = "locationId", source = "location.id")
  HourlyWeatherId toEntity(HourlyData hourlyData, Location location);
}
