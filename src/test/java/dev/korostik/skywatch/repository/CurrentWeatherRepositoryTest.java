package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Import(TestContainerConfiguration.class)
@Sql(scripts = {"/test-location.sql", "/provider-conditions-code.sql"})
class CurrentWeatherRepositoryTest {

  @Autowired
  private CurrentWeatherRepository repository;

  @Autowired
  private LocationRepository locationRepo;

  @Autowired
  private WeatherConditionRepository conditionRepo;

  @Test
  @DisplayName("Should save and retrieve current weather")
  void saveAndRetrieve() {
    // given
    Location loc = locationRepo.findById(1L).orElseThrow();
    WeatherCondition condition = conditionRepo.findById(1L).orElseThrow();

    CurrentWeather weather = CurrentWeather.builder()
        .location(loc)
        .weatherCondition(condition)
        .temperature(5.0)
        .windSpeed(3.0)
        .windDirection(180)
        .humidity(75)
        .pressure(1013.25)
        .build();

    // when
    CurrentWeather saved = repository.save(weather);

    // then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getLocation()).isNotNull();
    assertThat(saved.getWeatherCondition()).isNotNull();
    assertThat(saved.getTemperature()).isEqualTo(5.0);
    assertThat(saved.getWindSpeed()).isEqualTo(3.0);
    assertThat(saved.getWindDirection()).isEqualTo(180);
    assertThat(saved.getHumidity()).isEqualTo(75);
    assertThat(saved.getPressure()).isEqualTo(1013.25);
    assertThat(repository.findById(saved.getId())).isPresent();
  }
}
