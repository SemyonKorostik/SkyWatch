package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.List;
import java.util.Optional;
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
@Sql(scripts = "/provider-conditions-code.sql")
class WeatherProviderRepositoryTest {

  @Autowired
  private WeatherProviderRepository repository;

  @Test
  @DisplayName("Should save and find provider by enum")
  void saveAndFindByProvider() {
    // given
    WeatherProvider provider = WeatherProvider.builder()
        .provider(ForecastApiProvider.OPEN_METEO)
        .apiUrl("https://api.open-meteo.com/v1")
        .priority(1)
        .isEnabled(true)
        .build();

    // when
    repository.save(provider);

    // then
    Optional<WeatherProvider> result = repository.findByProvider(ForecastApiProvider.OPEN_METEO);
    assertThat(result).isPresent();
    assertThat(result.get().getApiUrl()).isEqualTo("https://api.open-meteo.com/v1");
    assertThat(result.get().getPriority()).isEqualTo(1);
    assertThat(result.get().getIsEnabled()).isTrue();
  }

  @Test
  @DisplayName("Should return enabled providers ordered by priority")
  void findAllEnabledOrdered() {
    // given
    repository.save(WeatherProvider.builder()
        .provider(ForecastApiProvider.OPEN_METEO)
        .apiUrl("https://api.open-meteo.com/v1")
        .priority(1)
        .isEnabled(true)
        .build());

    // when
    List<WeatherProvider> result = repository.findAllByIsEnabledOrderByPriorityAsc(true);

    // then
    assertThat(result).isNotEmpty();
  }
}
