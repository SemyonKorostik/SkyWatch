package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.LocationName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationNameMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "country", source = "countryName")
  @Mapping(target = "placeName", source = "name")
  @Mapping(target = "location", ignore = true)
  LocationName mapToEntity(GeoNameDto geoNameDto);

}
