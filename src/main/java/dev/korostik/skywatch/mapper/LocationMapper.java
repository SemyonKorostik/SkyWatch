package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationNameMapper.class, imports = ZoneId.class)
public interface LocationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "timeZoneId", expression = "java(ZoneId.of(dto.timeZone().timeZoneId()))")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Location mapToEntity(GeoNameDto dto);

}
