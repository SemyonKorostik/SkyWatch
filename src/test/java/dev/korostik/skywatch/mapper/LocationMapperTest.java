package dev.korostik.skywatch.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.Location;
import java.time.DateTimeException;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class LocationMapperTest {

  private final LocationMapper mapper = new LocationMapperImpl();

  @Test
  void mapToEntity_mapsAllFields() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(3, "Europe/Minsk", 3)
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getId());
    assertNull(result.getCreatedAt());
    assertNull(result.getUpdatedAt());
    assertEquals(53.9045, result.getLatitude());
    assertEquals(27.5615, result.getLongitude());
    assertEquals(ZoneId.of("Europe/Minsk"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_nullTimeZone_returnsNullZoneId() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL", null
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getTimeZoneId());
  }

  @Test
  void mapToEntity_nullDto_returnsNull() {
    assertNull(mapper.mapToEntity(null));
  }

  @Test
  void mapToEntity_extremeCoordinates_mapsCorrectly() {
    GeoNameDto dto = new GeoNameDto(
        "Extreme", -90.0, -180.0, "Land", "P", "PPL",
        new TimeZoneDto(0, "UTC", 0)
    );

    Location result = mapper.mapToEntity(dto);

    assertEquals(-90.0, result.getLatitude());
    assertEquals(-180.0, result.getLongitude());
    assertEquals(ZoneId.of("UTC"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_nullLatitudeAndLongitude_mapsNull() {
    GeoNameDto dto = new GeoNameDto(
        "Nowhere", null, null, "Land", "P", "PPL",
        new TimeZoneDto(0, "UTC", 0)
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getLatitude());
    assertNull(result.getLongitude());
  }

}
