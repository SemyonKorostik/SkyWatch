package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "timeZoneId", source = "timeZone")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Location mapToEntity(GeoNameDto dto);

  default ZoneId mapTimeZone(TimeZoneDto timeZone) {
    return timeZone == null ? null : ZoneId.of(timeZone.timeZoneId());
  }
}
