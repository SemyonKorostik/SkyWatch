package dev.korostik.skywatch.mapper.struct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.korostik.skywatch.dto.weather.external.feignclient.CurrentDataDto;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CurrentWeatherMapperTest extends MapperTestBase {

  private Location location;
  private Map<String, WeatherCondition> conditionMap;

  @BeforeEach
  void setUp() {
    location = createTestLocation();
    conditionMap = createWeatherConditionMap();
  }

  // --- Test Data Builder ---ForecastServiceImplTest.java:80
  static class TestData {

    static CurrentDataDto dto() {
      return new CurrentDataDto(
          LocalDateTime.of(2024, 1, 15, 12, 0, 0),
          60,
          5.5,
          80,
          PROVIDER_CODE_CLEAR,
          3.2,
          4.1,
          180,
          1013.2
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
      assertThat(entity.getId()).isNull();
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();

      // Context fields
      assertThat(entity.getLocation()).isSameAs(location);

      // Direct mappings
      assertThat(entity.getTemperature()).isEqualTo(dto.temperature());
      assertThat(entity.getHumidity()).isEqualTo(dto.relativeHumidity());
      assertThat(entity.getPressure()).isEqualTo(dto.surfacePressure());
      assertThat(entity.getWindSpeed()).isEqualTo(dto.windSpeed());
      assertThat(entity.getWindDirection()).isEqualTo(dto.windDirection());

      // Expression mapping (WeatherCondition resolution)
      assertThat(entity.getWeatherCondition()).isNotNull();
      assertThat(entity.getWeatherCondition().getProviderCode()).isEqualTo(PROVIDER_CODE_CLEAR);
      assertThat(entity.getWeatherCondition().getId()).isEqualTo(10L);
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
          LocalDateTime.of(2024, 1, 15, 12, 0, 0),
          60, 10.0, 50, "999", 5.0, 2.0, 90, 1000.0
      );

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
      assertThat(dto.relativeHumidity()).isEqualTo(entity.getHumidity());
      assertThat(dto.surfacePressure()).isEqualTo(entity.getPressure());
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
          .weatherCondition(null)
          .build();

      assertThatNullPointerException().isThrownBy(() -> currentWeatherMapper.toDto(entity));
    }
  }
}
