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
import org.mapstruct.factory.Mappers;

class LocationMapperTest {

  private final LocationMapper mapper = Mappers.getMapper(LocationMapper.class);

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

    Location result = mapper.mapToEntity(dto);

    assertNull(result.getId());
    assertNull(result.getCreatedAt());
    assertNull(result.getUpdatedAt());
    assertEquals(dto.latitude(), result.getLatitude());
    assertEquals(dto.longitude(), result.getLongitude());
    assertEquals(ZoneId.of("Europe/Minsk"), result.getTimeZoneId());
  }

  @Test
  void mapToEntity_invalidTimeZone_throwsException() {
    GeoNameDto dto = new GeoNameDto(
        "Minsk", 53.9045, 27.5615, "Belarus", "P", "PPL",
        new TimeZoneDto(99, "Invalid/Zone", 99)
    );
    assertThrows(DateTimeException.class, () -> mapper.mapToEntity(dto));
  }
}
