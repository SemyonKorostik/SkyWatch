package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.external.CurrentDataDto;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CurrentWeatherMapperTest extends MapperTestBase {

    private Location location;
    private Map<String, WeatherCondition> conditionMap;

    @BeforeEach
    void setUp() {
        location = createTestLocation();
        conditionMap = createWeatherConditionMap();
    }

    // --- Test Data Builder ---
    static class TestData {
        static CurrentDataDto dto() {
            // time и interval игнорируются в toEntity, но нужны для toDto
            return new CurrentDataDto(
                    "2024-01-15T12:00:00", // time (String)
                    60,                    // interval
                    5.5,                   // temperature
                    80,                    // relativeHumidity
                    PROVIDER_CODE_CLEAR,   // weatherCode
                    3.2,                   // apparentTemperature
                    4.1,                   // windSpeed
                    180,                   // windDirection
                    1013.2                 // surfacePressure
            );
        }

        static CurrentWeather entity() {
            WeatherCondition cond = WeatherCondition.builder()
                    .providerCode(PROVIDER_CODE_CLEAR)
                    .build();
            return CurrentWeather.builder()
                    .location(createTestLocationStatic())
                    .weatherCondition(cond)
                    .temperature(5.5)
                    .humidity(80)
                    .pressure(1013.2)
                    .windSpeed(4.1)
                    .windDirection(180)
                    .build();
        }

        // Статический хелпер для Location, чтобы не зависеть от инстанса теста
        static Location createTestLocationStatic() {
            return Location.builder()
                    .id(TEST_LOCATION_ID)
                    .latitude(TEST_LAT)
                    .longitude(TEST_LON)
                    .timeZoneId(TEST_ZONE_ID)
                    .build();
        }
    }

    @Nested
    @DisplayName("toEntity (DTO -> Entity)")
    class ToEntityTests {

        @Test
        @DisplayName("Should map all fields correctly including context resolution")
        void toEntity_happyPath_mapsAllFields() {
            // Given
            CurrentDataDto dto = TestData.dto();

            // When
            CurrentWeather entity = currentWeatherMapper.toEntity(dto, location, conditionMap);

            // Then
            assertThat(entity).isNotNull();
            
            // Ignored fields
            assertThat(entity.getId()).isNull(); // @Mapping(target = "id", ignore = true)
            assertThat(entity.getCreatedAt()).isNull();
            assertThat(entity.getUpdatedAt()).isNull();

            // Context fields
            assertThat(entity.getLocation()).isSameAs(location); // expression = "java(location)"

            // Direct mappings
            assertThat(entity.getTemperature()).isEqualTo(dto.temperature());
            assertThat(entity.getHumidity()).isEqualTo(dto.relativeHumidity()); // source = "relativeHumidity"
            assertThat(entity.getPressure()).isEqualTo(dto.surfacePressure());   // source = "surfacePressure"
            assertThat(entity.getWindSpeed()).isEqualTo(dto.windSpeed());
            assertThat(entity.getWindDirection()).isEqualTo(dto.windDirection());

            // Expression mapping (WeatherCondition resolution)
            assertThat(entity.getWeatherCondition()).isNotNull();
            assertThat(entity.getWeatherCondition().getProviderCode()).isEqualTo(PROVIDER_CODE_CLEAR);
            assertThat(entity.getWeatherCondition().getId()).isEqualTo(10L); // Из нашей мапы
        }

        @Test
        @DisplayName("Should return null if input DTO is null")
        void toEntity_nullInput_returnsNull() {
            CurrentWeather entity = currentWeatherMapper.toEntity(null, location, conditionMap);
            assertThat(entity).isNull();
        }

        @Test
        @DisplayName("Should handle missing WeatherCondition in map (returns null condition)")
        void toEntity_missingConditionCode_setsConditionNull() {
            CurrentDataDto dto = new CurrentDataDto(
                    "time", 60, 10.0, 50, "999", 5.0, 2.0, 90, 1000.0
            ); // Code "999" нет в мапе

            CurrentWeather entity = currentWeatherMapper.toEntity(dto, location, conditionMap);

            assertThat(entity.getWeatherCondition()).isNull();
        }
    }

    @Nested
    @DisplayName("toDto (Entity -> DTO)")
    class ToDtoTests {

        @Test
        @DisplayName("Should map all fields correctly reverse mapping")
        void toDto_happyPath_mapsAllFields() {
            // Given
            CurrentWeather entity = TestData.entity();

            // When
            CurrentDataDto dto = currentWeatherMapper.toDto(entity);

            // Then
            assertThat(dto).isNotNull();
            
            // Ignored in toDto
            assertThat(dto.time()).isNull();
            assertThat(dto.interval()).isNull();
            assertThat(dto.apparentTemperature()).isNull();

            // Mapped fields
            assertThat(dto.temperature()).isEqualTo(entity.getTemperature());
            assertThat(dto.relativeHumidity()).isEqualTo(entity.getHumidity()); // source = "humidity"
            assertThat(dto.surfacePressure()).isEqualTo(entity.getPressure());   // source = "pressure"
            assertThat(dto.windSpeed()).isEqualTo(entity.getWindSpeed());
            assertThat(dto.windDirection()).isEqualTo(entity.getWindDirection());

            // Expression: weatherCode from WeatherCondition.providerCode
            assertThat(dto.weatherCode()).isEqualTo(PROVIDER_CODE_CLEAR);
        }

        @Test
        @DisplayName("Should return null if input Entity is null")
        void toDto_nullInput_returnsNull() {
            assertThat(currentWeatherMapper.toDto(null)).isNull();
        }

        @Test
        @DisplayName("Should throw NPE if WeatherCondition is null in entity (expression access)")
        void toDto_nullWeatherCondition_throwsNPE() {
            CurrentWeather entity = CurrentWeather.builder()
                    .location(TestData.createTestLocationStatic())
                    .temperature(1.0)
                    .humidity(1)
                    .pressure(1.0)
                    .windSpeed(1.0)
                    .windDirection(1)
                    .weatherCondition(null) // Важно: null
                    .build();

            // MapStruct генерирует прямой вызов геттера, будет NPE
            assertThatNullPointerException().isThrownBy(() -> currentWeatherMapper.toDto(entity));
        }
    }
}
