package dev.korostik.skywatch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeatherId;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(TestContainerConfiguration.class)
@Sql(scripts = {"/test-location.sql", "/provider-conditions-code.sql"})
class HourlyWeatherRepositoryTest {

  @Autowired
  private HourlyWeatherRepository repository;

  @Autowired
  private LocationRepository locationRepo;

  @Autowired
  private WeatherConditionRepository weatherConditionRepo;

  @Test
  @DisplayName("Should save and retrieve hourly weather")
  void saveAndRetrieve() {
    // given
    Location loc = locationRepo.findById(1L).orElseThrow();
    WeatherCondition weatherCondition = weatherConditionRepo.findById(1L).orElseThrow();

    HourlyWeather weather = HourlyWeather.builder()
        .id(HourlyWeatherId.builder()
            .locationId(loc.getId())
            .time(Instant.now())
            .build())
        .location(loc)
        .weatherCondition(weatherCondition)
        .temperature(8.5)
        .pressure(1010.0)
        .windSpeed(5.5)
        .windDirection(180)
        .precipitation(0.5)
        .precipitationProbability(40)
        .build();

    // when
    repository.save(weather);

    // then
    assertThat(repository.findAll()).isNotEmpty();
  }
}
