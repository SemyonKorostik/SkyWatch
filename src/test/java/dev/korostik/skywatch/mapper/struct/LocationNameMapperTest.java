package dev.korostik.skywatch.mapper.struct;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.dto.geonames.TimeZoneDto;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.mapper.MapperTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationNameMapperTest extends MapperTestBase {

    private LocationNameMapper locationNameMapper;

    @BeforeEach
    void setUp() {
        locationNameMapper = Mappers.getMapper(LocationNameMapper.class);
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

        static GeoNameDto dtoWithNullNames() {
            return new GeoNameDto(
                    null,
                    TEST_LAT,
                    TEST_LON,
                    null,
                    "P",
                    "PPL",
                    new TimeZoneDto("Europe/Minsk")
            );
        }
    }

    @Nested
    @DisplayName("mapToEntity (DTO -> Entity)")
    class ToEntityTests {

        @Test
        @DisplayName("Should map placeName and country correctly, ignore id and location")
        void mapToEntity_validDto_mapsAllFields() {
            // Given
            GeoNameDto dto = TestData.validDto();

            // When
            LocationName entity = locationNameMapper.mapToEntity(dto);

            // Then
            assertThat(entity).isNotNull();

            // Ignored fields
            assertThat(entity.getId()).isNull();
            assertThat(entity.getLocation()).isNull();

            // Mapped fields (with renaming)
            assertThat(entity.getPlaceName()).isEqualTo("Minsk");   // source = "name"
            assertThat(entity.getCountry()).isEqualTo("Belarus");   // source = "countryName"
        }

        @Test
        @DisplayName("Should return null if input DTO is null")
        void mapToEntity_nullInput_returnsNull() {
            // When
            LocationName entity = locationNameMapper.mapToEntity(null);

            // Then
            assertThat(entity).isNull();
        }

        @Test
        @DisplayName("Should handle null placeName and countryName gracefully")
        void mapToEntity_nullNames_mapsAsNull() {
            // Given
            GeoNameDto dto = TestData.dtoWithNullNames();

            // When
            LocationName entity = locationNameMapper.mapToEntity(dto);

            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getPlaceName()).isNull();
            assertThat(entity.getCountry()).isNull();
        }
    }
}
