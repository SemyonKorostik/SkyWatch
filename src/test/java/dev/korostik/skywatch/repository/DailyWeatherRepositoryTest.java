package dev.korostik.skywatch.repository;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.DailyWeatherId;
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
class DailyWeatherRepositoryTest {

  @Autowired
  private DailyWeatherRepository repository;

  @Autowired
  private LocationRepository locationRepo;

  @Autowired
  private WeatherConditionRepository weatherConditionRepo;

  @Test
  @DisplayName("Should save and retrieve daily weather")
  void saveAndRetrieve() {
    // given
    Location loc = locationRepo.findById(1L).orElseThrow();
    WeatherCondition weatherCondition = weatherConditionRepo.findById(1L).orElseThrow();

    DailyWeather weather = DailyWeather.builder()
        .id(DailyWeatherId.builder()
            .locationId(loc.getId())
            .date(Instant.now())
            .build())
        .location(loc)
        .weatherCondition(weatherCondition)
        .temperatureMax(15.5)
        .temperatureMin(-2.0)
        .windSpeed(4.5)
        .windDirection(270)
        .humidity(60)
        .build();

    // when
    repository.save(weather);

    // then
    assertThat(repository.findAll()).isNotEmpty();
  }
}
