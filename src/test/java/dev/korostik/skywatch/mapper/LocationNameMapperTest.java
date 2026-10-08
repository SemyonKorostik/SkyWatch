package dev.korostik.skywatch.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.LocationName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationNameMapperTest {

  private final LocationNameMapper mapper = Mappers.getMapper(LocationNameMapper.class);

  @Test
  void mapToEntity() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk",
        53.9045,
        27.5615,
        "Belarus",
        "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3)
    );

    LocationName result = mapper.mapToEntity(dto);

    assertNull(result.getId());
    assertNull(result.getLocation());
    assertEquals("Minsk", result.getPlaceName());
    assertEquals("Belarus", result.getCountry());
  }
}
