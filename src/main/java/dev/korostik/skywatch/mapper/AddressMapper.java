package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  @Mapping(target = "country", source = "countryName")
  @Mapping(target = "placeName", source = "name")
  Address mapToEntity(GeoNameDto geoNameDto);

}
