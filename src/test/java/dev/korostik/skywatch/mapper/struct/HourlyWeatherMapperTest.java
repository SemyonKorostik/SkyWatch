package dev.korostik.skywatch.mapper.struct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeatherId;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HourlyWeatherMapperTest extends MapperTestBase {

  private Location location;
  private Map<String, WeatherCondition> conditionMap;
  private final LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 15, 12, 0); // 12:00
  private Instant expectedInstant; // 12:00 Europe/Minsk -> Instant

  @BeforeEach
  void setUp() {
    location = createTestLocation();
    conditionMap = createWeatherConditionMap();
    expectedInstant = testDateTime.atZone(TEST_ZONE_ID).toInstant();
  }

  static class TestData {

    static HourlyData dto(LocalDateTime time, String weatherCode) {
      return HourlyData.builder()
          .time(time)
          .temperature(2.5)
          .relativeHumidity(90)
          .weatherCode(weatherCode)
          .apparentTemperature(0.0)
          .precipitation(1.2)
          .precipitationProbability(80)
          .windSpeed(5.5)
          .windDirection(270)
          .surfacePressure(998.5)
          .build();
    }

    static HourlyWeather entity(Instant timeInstant, WeatherCondition condition) {
      return HourlyWeather.builder()
          .id(new HourlyWeatherId(timeInstant, TEST_LOCATION_ID))
          .location(createTestLocationStatic())
          .weatherCondition(condition)
          .temperature(2.5)
          .humidity(90)
          .pressure(998.5)
          .windSpeed(5.5)
          .windDirection(270)
          .precipitation(1.2)
          .precipitationProbability(80)
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
    @DisplayName("Should map all fields, convert LocalDateTime to Instant via ZoneId, resolve WeatherCondition")
    void toEntity_happyPath_mapsAllFields() {
      HourlyData dto = TestData.dto(testDateTime, PROVIDER_CODE_RAIN);

      HourlyWeather entity = hourlyWeatherMapper.toEntity(dto, location, conditionMap);

      assertThat(entity).isNotNull();

      // ID (EmbeddedId)
      assertThat(entity.getId()).isNotNull();
      assertThat(entity.getId().getLocationId()).isEqualTo(location.getId());
      assertThat(entity.getId().getTime()).isEqualTo(expectedInstant);

      // Ignored
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();

      // Context
      assertThat(entity.getLocation()).isSameAs(location);
      assertThat(entity.getWeatherCondition()).isSameAs(
          conditionMap.get(entity.getWeatherCondition().getProviderCode()));

      // Direct mappings (with renaming)
      assertThat(entity.getTemperature()).isEqualTo(dto.temperature());
      assertThat(entity.getHumidity()).isEqualTo(
          dto.relativeHumidity());
      assertThat(entity.getPressure()).isEqualTo(
          dto.surfacePressure());
      assertThat(entity.getWindSpeed()).isEqualTo(dto.windSpeed());
      assertThat(entity.getWindDirection()).isEqualTo(dto.windDirection());
      assertThat(entity.getPrecipitation()).isEqualTo(dto.precipitation());
      assertThat(entity.getPrecipitationProbability()).isEqualTo(dto.precipitationProbability());

      // Expression: WeatherCondition
      assertThat(entity.getWeatherCondition()).isNotNull();
      assertThat(entity.getWeatherCondition().getProviderCode()).isEqualTo(PROVIDER_CODE_RAIN);
      assertThat(entity.getWeatherCondition().getId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Should convert each DTO to Entity")
    void toEntities_listInput_mapsAll() {
      List<HourlyData> dtos = List.of(
          TestData.dto(testDateTime, PROVIDER_CODE_CLEAR),
          TestData.dto(testDateTime.plusHours(1), PROVIDER_CODE_RAIN)
      );

      List<HourlyWeather> entities = hourlyWeatherMapper.toEntities(dtos, location, conditionMap);

      assertThat(entities).hasSize(2);
      assertThat(entities.get(0).getId().getTime()).isEqualTo(expectedInstant);
      assertThat(entities.get(1).getId().getTime()).isEqualTo(
          testDateTime.plusHours(1).atZone(TEST_ZONE_ID).toInstant());
    }

    @Test
    @DisplayName("Should return null on null input")
    void toEntity_nullInput_returnsNull() {
      assertThat(hourlyWeatherMapper.toEntity(null, location, conditionMap)).isNull();
    }
  }

  @Nested
  @DisplayName("toHourlyData (Entity -> DTO)")
  class ToDtoTests {

    @Test
    @DisplayName("Should reverse map: Instant -> LocalDateTime (in location ZoneId), WeatherCondition -> providerCode, rename fields")
    void toHourlyData_happyPath_reverseMaps() {
      WeatherCondition cond = conditionMap.get(PROVIDER_CODE_SNOW);
      HourlyWeather entity = TestData.entity(expectedInstant, cond);

      HourlyData dto = hourlyWeatherMapper.toHourlyData(entity);

      assertThat(dto).isNotNull();

      // Ignored in reverse
      assertThat(dto.apparentTemperature()).isNull();

      // Time conversion: Instant -> LocalDateTime in location ZoneId
      assertThat(dto.time()).isEqualTo(testDateTime);

      assertThat(dto.weatherCode()).isEqualTo(PROVIDER_CODE_SNOW);

      // Renaming back
      assertThat(dto.relativeHumidity()).isEqualTo(entity.getHumidity());
      assertThat(dto.surfacePressure()).isEqualTo(entity.getPressure());

      // Direct
      assertThat(dto.temperature()).isEqualTo(entity.getTemperature());
      assertThat(dto.windSpeed()).isEqualTo(entity.getWindSpeed());
      assertThat(dto.windDirection()).isEqualTo(entity.getWindDirection());
      assertThat(dto.precipitation()).isEqualTo(entity.getPrecipitation());
      assertThat(dto.precipitationProbability()).isEqualTo(entity.getPrecipitationProbability());
    }

    @Test
    @DisplayName("Should convert each Entity to DTO")
    void toHourlyDataList_listInput_mapsAll() {
      WeatherCondition cond = conditionMap.get(PROVIDER_CODE_CLEAR);
      List<HourlyWeather> entities = List.of(
          TestData.entity(expectedInstant, cond),
          TestData.entity(testDateTime.plusHours(1).atZone(TEST_ZONE_ID).toInstant(), cond)
      );

      List<HourlyData> dtos = hourlyWeatherMapper.toHourlyDataList(entities);

      assertThat(dtos).hasSize(2);
      // Проверяем конвертацию в ZoneId локации
      assertThat(dtos.get(0).time()).isEqualTo(testDateTime);
      assertThat(dtos.get(1).time()).isEqualTo(testDateTime.plusHours(1));
    }

    @Test
    @DisplayName("Should return null on null input")
    void toHourlyData_nullInput_returnsNull() {
      assertThat(hourlyWeatherMapper.toHourlyData(null)).isNull();
    }

    @Test
    @DisplayName("Should throw NPE when Entity's weather condition is null")
    void toHourlyData_nullCondition_throwsNPE() {
      HourlyWeather entity = HourlyWeather.builder()
          .id(new HourlyWeatherId(expectedInstant, TEST_LOCATION_ID))
          .location(TestData.createTestLocationStatic())
          .temperature(1.0)
          .humidity(1)
          .pressure(1.0)
          .windSpeed(1.0)
          .windDirection(1)
          .precipitation(1.0)
          .precipitationProbability(1)
          .weatherCondition(null)
          .build();

      assertThatNullPointerException().isThrownBy(() -> hourlyWeatherMapper.toHourlyData(entity));
    }
  }
}
