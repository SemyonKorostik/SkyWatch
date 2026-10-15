package dev.korostik.skywatch.mapper.struct;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationMapperTest extends MapperTestBase {

  private LocationMapper locationMapper;

  @BeforeEach
  void setUp() {
    locationMapper = Mappers.getMapper(LocationMapper.class);
  }

  // --- Test Data Builder ---
  static class TestData {

    static GeoNameDto validDto() {
      return new GeoNameDto(
          "Minsk",
          TEST_LAT,
          TEST_LON,
          "Belarus",
          "P",
          "PPL",
          new TimeZoneDto("Europe/Minsk")
      );
    }

    static GeoNameDto dtoWithNullTimeZone() {
      return new GeoNameDto(
          "Minsk",
          TEST_LAT,
          TEST_LON,
          "Belarus",
          "P",
          "PPL",
          null
      );
    }

    static GeoNameDto dtoWithNullCoordinates() {
      return new GeoNameDto(
          "Nowhere",
          null,
          null,
          "Land",
          "P",
          "PPL",
          new TimeZoneDto("UTC")
      );
    }

    static GeoNameDto dtoMinCoordinates() {
      return new GeoNameDto(
          "Extreme",
          -90.0,
          -180.0,
          "Land",
          "P",
          "PPL",
          new TimeZoneDto("UTC")
      );
    }

    static GeoNameDto dtoMaxCoordinates() {
      return new GeoNameDto(
          "Extreme",
          90.0,
          180.0,
          "Land",
          "P",
          "PPL",
          new TimeZoneDto("UTC")
      );
    }
  }

  @Nested
  @DisplayName("mapToEntity (DTO -> Entity)")
  class ToEntityTests {

    @Test
    @DisplayName("Should map all fields correctly including timeZone conversion")
    void mapToEntity_validDto_mapsAllFields() {
      // Given
      GeoNameDto dto = TestData.validDto();

      // When
      Location entity = locationMapper.mapToEntity(dto);

      // Then
      assertThat(entity).isNotNull();

      // Ignored fields
      assertThat(entity.getId()).isNull();
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();

      // Mapped fields
      assertThat(entity.getLatitude()).isEqualTo(TEST_LAT);
      assertThat(entity.getLongitude()).isEqualTo(TEST_LON);
      assertThat(entity.getTimeZoneId()).isEqualTo(TEST_ZONE_ID);
    }

    @Test
    @DisplayName("Should return null if input DTO is null")
    void mapToEntity_nullInput_returnsNull() {
      // When
      Location entity = locationMapper.mapToEntity(null);

      // Then
      assertThat(entity).isNull();
    }

    @Test
    @DisplayName("Should handle null TimeZoneDto gracefully")
    void mapToEntity_nullTimeZone_returnsNullZoneId() {
      // Given
      GeoNameDto dto = TestData.dtoWithNullTimeZone();

      // When
      Location entity = locationMapper.mapToEntity(dto);

      // Then
      assertThat(entity).isNotNull();
      assertThat(entity.getTimeZoneId()).isNull();
      assertThat(entity.getLatitude()).isEqualTo(TEST_LAT);
      assertThat(entity.getLongitude()).isEqualTo(TEST_LON);
    }

    @Test
    @DisplayName("Should handle null latitude and longitude")
    void mapToEntity_nullCoordinates_mapsAsNull() {
      // Given
      GeoNameDto dto = TestData.dtoWithNullCoordinates();

      // When
      Location entity = locationMapper.mapToEntity(dto);

      // Then
      assertThat(entity).isNotNull();
      assertThat(entity.getLatitude()).isNull();
      assertThat(entity.getLongitude()).isNull();
      assertThat(entity.getTimeZoneId()).isEqualTo(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("Should map minimum extreme coordinates correctly")
    void mapToEntity_minCoordinates_mapsCorrectly() {
      // Given
      GeoNameDto dto = TestData.dtoMinCoordinates();

      // When
      Location entity = locationMapper.mapToEntity(dto);

      // Then
      assertThat(entity.getLatitude()).isEqualTo(-90.0);
      assertThat(entity.getLongitude()).isEqualTo(-180.0);
      assertThat(entity.getTimeZoneId()).isEqualTo(ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("Should map maximum extreme coordinates correctly")
    void mapToEntity_maxCoordinates_mapsCorrectly() {
      // Given
      GeoNameDto dto = TestData.dtoMaxCoordinates();

      // When
      Location entity = locationMapper.mapToEntity(dto);

      // Then
      assertThat(entity.getLatitude()).isEqualTo(90.0);
      assertThat(entity.getLongitude()).isEqualTo(180.0);
      assertThat(entity.getTimeZoneId()).isEqualTo(ZoneId.of("UTC"));
    }
  }
}
