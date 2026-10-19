package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.List;
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
class WeatherConditionRepositoryTest {

  @Autowired
  private WeatherConditionRepository repository;

  @Autowired
  private WeatherProviderRepository providerRepository;

  @Test
  @DisplayName("Should save and find weather conditions by provider")
  void saveAndFindByProvider() {
    // given
    WeatherProvider provider = providerRepository.findByProvider(ForecastApiProvider.OPEN_METEO)
        .orElseThrow();

    // when
    List<WeatherCondition> result = repository.findAllByProviderName(provider);

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getProviderName()).isNotNull();
    assertThat(result.get(0).getProviderCode()).isNotNull();
    assertThat(result.get(0).getWmoCode()).isNotNull();
  }
}
