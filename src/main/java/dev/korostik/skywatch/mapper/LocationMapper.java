package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LocationMapper {

/*    @Mapping(target = "timeZoneOffset", source = "timeZone.gmtOffset")
    @Mapping(target = "address", source = "timeZone.gmtOffset")
    Location mapToEntity(GeoNameDto dto);*/
}
