package dev.korostik.skywatch.service.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import dev.korostik.skywatch.config.TestContainerConfiguration;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.exception.WeatherProviderNotFoundException;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(TestContainerConfiguration.class)
class WeatherProviderServiceImplTest {

  @Autowired
  private WeatherProviderServiceImpl service;

  @Autowired
  private WeatherProviderRepository repository;

  @Nested
  @DisplayName("getAllEnabledOrderedByPriority")
  class GetByProviderTests {

    @Test
    @DisplayName("Should return provider by enum")
    void getByProvider_returnsEntity() {
      repository.save(WeatherProvider.builder()
          .apiUrl("https://api.open-meteo.com")
          .provider(ForecastApiProvider.OPEN_METEO)
          .isEnabled(true)
          .priority(1)
          .build());
      WeatherProvider result = service.getByProvider(ForecastApiProvider.OPEN_METEO);
      assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Should return empty list if there is no providers")
    void getByProvide_returnsEmptyList() {
      assertThatExceptionOfType(WeatherProviderNotFoundException.class)
          .isThrownBy(() -> service.getByProvider(ForecastApiProvider.OPEN_METEO))
          .withMessage("Weather provider not found: open-meteo");
    }
  }

  @Nested
  @DisplayName("getAllEnabledOrderedByPriority")
  class GetAllEnabledOrderedByPriorityTests {

    @Test
    @DisplayName("Should return enabled providers ordered by priority")
    void getAllEnabledOrderedByPriority_returnsOrdered() {
      repository.save(WeatherProvider.builder()
          .apiUrl("https://api.open-meteo.com")
          .provider(ForecastApiProvider.OPEN_METEO)
          .isEnabled(true)
          .priority(1)
          .build());

      List<WeatherProvider> result = service.getAllEnabledOrderedByPriority();
      assertThat(result).isNotEmpty();
      assertThat(result.getFirst().getProvider()).isEqualTo(ForecastApiProvider.OPEN_METEO);
    }

    @Test
    @DisplayName("Should return empty list if there is no providers")
    void getAllEnabledProviders_returnsEmptyList() {
      List<WeatherProvider> result = service.getAllEnabledOrderedByPriority();
      assertThat(result).isEmpty();
    }
  }

}
