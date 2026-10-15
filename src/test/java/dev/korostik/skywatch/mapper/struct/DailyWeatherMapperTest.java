package dev.korostik.skywatch.mapper.struct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import dev.korostik.skywatch.dto.weather.transit.DailyData;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.DailyWeatherId;
import dev.korostik.skywatch.mapper.MapperTestBase;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DailyWeatherMapperTest extends MapperTestBase {

  private Location location;
  private Map<String, WeatherCondition> conditionMap;
  private final LocalDate testDate = LocalDate.of(2024, 1, 15);
  private Instant expectedInstant; // 2024-01-15 00:00 Europe/Minsk -> Instant

  @BeforeEach
  void setUp() {
    location = createTestLocation();
    conditionMap = createWeatherConditionMap();
    expectedInstant = testDate.atStartOfDay(TEST_ZONE_ID).toInstant();
  }

  static class TestData {

    static DailyData dto(LocalDate date, String weatherCode) {
      return DailyData.builder()
          .date(date)
          .temperatureMax(10.5)
          .temperatureMin(-2.3)
          .weatherCode(weatherCode)
          .build();
    }

    static DailyWeather entity(Instant dateInstant, WeatherCondition condition) {
      return DailyWeather.builder()
          .id(new DailyWeatherId(TEST_LOCATION_ID, dateInstant))
          .location(createTestLocationStatic())
          .weatherCondition(condition)
          .temperatureMax(10.5)
          .temperatureMin(-2.3)
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
    @DisplayName("Should map all fields, convert date to Instant via ZoneId, resolve WeatherCondition")
    void toEntity_happyPath_mapsAllFields() {
      DailyData dto = TestData.dto(testDate, PROVIDER_CODE_RAIN);

      DailyWeather entity = dailyWeatherMapper.toEntity(dto, location, conditionMap);

      assertThat(entity).isNotNull();

      // ID mapping (EmbeddedId)
      assertThat(entity.getId()).isNotNull();
      assertThat(entity.getId().getDate()).isEqualTo(expectedInstant);

      // Ignored
      assertThat(entity.getId().getLocationId()).isNull();
      assertThat(entity.getCreatedAt()).isNull();
      assertThat(entity.getUpdatedAt()).isNull();
      assertThat(entity.getWindSpeed()).isNull();
      assertThat(entity.getWindDirection()).isNull();
      assertThat(entity.getHumidity()).isNull();

      // Context
      assertThat(entity.getLocation()).isSameAs(location);
      assertThat(entity.getWeatherCondition()).isSameAs(
          conditionMap.get(entity.getWeatherCondition().getProviderCode()));

      // Direct
      assertThat(entity.getTemperatureMax()).isEqualTo(dto.temperatureMax());
      assertThat(entity.getTemperatureMin()).isEqualTo(dto.temperatureMin());

      // Expression: WeatherCondition
      assertThat(entity.getWeatherCondition()).isNotNull();
      assertThat(entity.getWeatherCondition().getProviderCode()).isEqualTo(PROVIDER_CODE_RAIN);
      assertThat(entity.getWeatherCondition().getId()).isEqualTo(11L);
    }

    @Test
    @DisplayName("Should map list of DTOs")
    void toEntities_listInput_mapsAll() {
      List<DailyData> dtos = List.of(
          TestData.dto(testDate, PROVIDER_CODE_CLEAR),
          TestData.dto(testDate.plusDays(1), PROVIDER_CODE_RAIN)
      );

      List<DailyWeather> entities = dailyWeatherMapper.toEntities(dtos, location, conditionMap);

      assertThat(entities).hasSize(2);
      assertThat(entities.get(0).getId().getDate()).isEqualTo(expectedInstant);
      assertThat(entities.get(1).getId().getDate()).isEqualTo(
          testDate.plusDays(1).atStartOfDay(TEST_ZONE_ID).toInstant());
      assertThat(entities.get(0).getWeatherCondition().getProviderCode()).isEqualTo(
          PROVIDER_CODE_CLEAR);
      assertThat(entities.get(1).getWeatherCondition().getProviderCode()).isEqualTo(
          PROVIDER_CODE_RAIN);
    }

    @Test
    void toEntity_nullInput_returnsNull() {
      assertThat(dailyWeatherMapper.toEntity(null, location, conditionMap)).isNull();
    }
  }

  @Nested
  @DisplayName("toDailyData (Entity -> DTO)")
  class ToDtoTests {

    @Test
    @DisplayName("Should reverse map: Instant -> LocalDate, WeatherCondition -> providerCode")
    void toDailyData_happyPath_reverseMaps() {
      WeatherCondition cond = conditionMap.get(PROVIDER_CODE_SNOW);
      DailyWeather entity = TestData.entity(expectedInstant, cond);

      DailyData dto = dailyWeatherMapper.toDailyData(entity);

      assertThat(dto).isNotNull();
      assertThat(dto.date()).isEqualTo(testDate);
      assertThat(dto.weatherCode()).isEqualTo(PROVIDER_CODE_SNOW);
      assertThat(dto.temperatureMax()).isEqualTo(entity.getTemperatureMax());
      assertThat(dto.temperatureMin()).isEqualTo(entity.getTemperatureMin());
    }

    @Test
    void toDailyDataList_listInput_mapsAll() {
      WeatherCondition cond = conditionMap.get(PROVIDER_CODE_CLEAR);
      List<DailyWeather> entities = List.of(
          TestData.entity(expectedInstant, cond),
          TestData.entity(testDate.plusDays(1).atStartOfDay(TEST_ZONE_ID).toInstant(), cond)
      );

      List<DailyData> dtos = dailyWeatherMapper.toDailyDataList(entities);

      assertThat(dtos).hasSize(2);
      assertThat(dtos.get(0).date()).isEqualTo(testDate);
      assertThat(dtos.get(1).date()).isEqualTo(testDate.plusDays(1));
    }

    @Test
    void toDailyData_nullInput_returnsNull() {
      assertThat(dailyWeatherMapper.toDailyData(null)).isNull();
    }

    @Test
    @DisplayName("NPE if WeatherCondition null in entity (expression access)")
    void toDailyData_nullCondition_throwsNPE() {
      DailyWeather entity = DailyWeather.builder()
          .id(new DailyWeatherId(TEST_LOCATION_ID, expectedInstant))
          .location(TestData.createTestLocationStatic())
          .temperatureMax(1.0)
          .temperatureMin(1.0)
          .weatherCondition(null)
          .build();

      assertThatNullPointerException().isThrownBy(() -> dailyWeatherMapper.toDailyData(entity));
    }
  }
}
