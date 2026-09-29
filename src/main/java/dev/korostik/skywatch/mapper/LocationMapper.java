package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeZone", expression = "java(ZoneOffset.of(timeZone.timeZoneId))")
    @Mapping(target = "address", source = ".")
    Location mapToEntity(GeoNameDto dto);

}
