package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeZoneOffset", source = "timeZone.gmtOffset")
    @Mapping(target = "address", source = ".")
    Location mapToEntity(GeoNameDto dto);

}
