package dev.korostik.skywatch.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.LocationName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationNameMapperTest {
/*
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

  @Test
  void mapToEntity_nullDto_returnsNull() {
    assertNull(mapper.mapToEntity(null));
  }

  @Test
  void mapToEntity_emptyStrings_mappedAsEmpty() {
    GeoNameDto dto = new GeoNameDto(
        "",
        0.0,
        0.0,
        "",
        "", "",
        new TimeZoneDto(0, "UTC", 0)
    );

    LocationName result = mapper.mapToEntity(dto);

    assertEquals("", result.getPlaceName());
    assertEquals("", result.getCountry());
  }

  @Test
  void mapToEntity_longStrings_mappedAsIs() {
    String longString = "a".repeat(200);
    GeoNameDto dto = new GeoNameDto(
        longString,
        0.0,
        0.0,
        longString,
        "P", "PPL",
        new TimeZoneDto(0, "UTC", 0)
    );

    LocationName result = mapper.mapToEntity(dto);

    assertEquals(longString, result.getPlaceName());
    assertEquals(longString, result.getCountry());
  }*/
}
