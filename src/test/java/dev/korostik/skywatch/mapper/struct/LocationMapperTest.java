package dev.korostik.skywatch.mapper.struct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.location.Location;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationMapperTest {

  public static final double LAT = 53.9045;
  public static final double LON = 27.5615;
  private final LocationMapper mapper = Mappers.getMapper(LocationMapper.class);

  @Test
  void mapToEntity_whenValidDto_thenMapsAllFields() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", LAT, LON, "Belarus", "P", "PPL",
        new TimeZoneDto("Europe/Minsk")
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getId());
    assertNull(result.getCreatedAt());
    assertNull(result.getUpdatedAt());
    assertEquals(LAT, result.getLatitude());
    assertEquals(LON, result.getLongitude());
    assertEquals(ZoneId.of("Europe/Minsk"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_whenNullTimeZone_thenReturnsNullZoneId() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", LAT, LON, "Belarus", "P", "PPL", null
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getTimeZoneId());
  }

  @Test
  void mapToEntity_whenNullDto_thenReturnsNull() {
    assertNull(mapper.mapToEntity(null));
  }

  @Test
  void mapToEntity_whenMinExtremeCoordinates_thenMapsCorrectly() {
    GeoNameDto dto = new GeoNameDto(
        "Extreme", -90.0, -180.0, "Land", "P", "PPL",
        new TimeZoneDto("UTC")
    );

    Location result = mapper.mapToEntity(dto);

    assertEquals(-90.0, result.getLatitude());
    assertEquals(-180.0, result.getLongitude());
    assertEquals(ZoneId.of("UTC"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_whenMaxExtremeCoordinates_thenMapsCorrectly() {
    GeoNameDto dto = new GeoNameDto(
        "Extreme", 90.0, 180.0, "Land", "P", "PPL",
        new TimeZoneDto("UTC")
    );

    Location result = mapper.mapToEntity(dto);

    assertEquals(90.0, result.getLatitude());
    assertEquals(180.0, result.getLongitude());
    assertEquals(ZoneId.of("UTC"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_whenNullLatitudeAndLongitude_thenMapsNull() {
    GeoNameDto dto = new GeoNameDto(
        "Nowhere", null, null, "Land", "P", "PPL",
        new TimeZoneDto("UTC")
    );

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getLatitude());
    assertNull(result.getLongitude());
  }

}
