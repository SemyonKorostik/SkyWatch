package dev.korostik.skywatch.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.LocationName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationNameMapperTest {

  public static final double LAT = 53.9045;
  public static final double LON = 27.5615;
  private final LocationNameMapper mapper = Mappers.getMapper(LocationNameMapper.class);

  @Test
  void mapToEntity_whenValidDto_thenMapsAllFields() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk",
        LAT,
        LON,
        "Belarus",
        "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );

    LocationName result = mapper.mapToEntity(dto);

    assertNull(result.getId());
    assertNull(result.getLocation());
    assertEquals("Minsk", result.getPlaceName());
    assertEquals("Belarus", result.getCountry());
  }

  @Test
  void mapToEntity_whenNullDto_thenReturnsNull() {
    assertNull(mapper.mapToEntity(null));
  }

  @Test
  void mapToEntity_whenNullCountryAndPlaceName_thenMapsAsNull() {
    GeoNameDto dto = new GeoNameDto(
        null,
        LAT,
        LON,
        null,
        "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );

    LocationName result = mapper.mapToEntity(dto);

    assertNull(result.getPlaceName());
    assertNull(result.getCountry());
  }
}
