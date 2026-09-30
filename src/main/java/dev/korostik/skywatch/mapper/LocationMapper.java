package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Location;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class, imports = ZoneId.class)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "zoneId", expression = "java(ZoneId.of(dto.timeZone().timeZoneId()))")
    @Mapping(target = "address", source = ".")
    Location mapToEntity(GeoNameDto dto);

}
