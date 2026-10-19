package dev.korostik.skywatch.service.weather;

import static org.assertj.core.api.Assertions.assertThat;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ForecastApiProviderResolverTest {

  private WeatherProviderService providerService;
  private WeatherConditionService conditionService;
  private ForecastApiClientProxy proxy;
  private ForecastApiProviderResolver resolver;

  @BeforeEach
  void setUp() {
    providerService = Mockito.mock(WeatherProviderService.class);
    conditionService = Mockito.mock(WeatherConditionService.class);
    proxy = Mockito.mock(ForecastApiClientProxy.class);
    Map<ForecastApiProvider, ForecastApiClientProxy> proxyMap = new HashMap<>();
    proxyMap.put(ForecastApiProvider.OPEN_METEO, proxy);
    resolver = new ForecastApiProviderResolver(providerService, proxyMap, conditionService);
  }

  @Nested
  @DisplayName("getAvailableProviders")
  class GetAvailableProvidersTests {

    @Test
    @DisplayName("Should return proxies for enabled providers with conditions")
    void getAvailableProviders_filtersCorrectly() {
      WeatherProvider wp = WeatherProvider.builder()
          .provider(ForecastApiProvider.OPEN_METEO).build();
      Mockito.when(providerService.getAllEnabledOrderedByPriority())
          .thenReturn(List.of(wp));
      Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
          .thenReturn(Map.of("0", new WeatherCondition()));
      Mockito.when(proxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);

      List<ForecastApiClientProxy> result = resolver.getAvailableProviders();

      assertThat(result).containsExactly(proxy);
    }
  }

  @Test
  @DisplayName("getForecastApiProvider should return first available provider")
  void getForecastApiProvider_returnsFirstAvailable() {
    WeatherProvider wp = WeatherProvider.builder()
        .provider(ForecastApiProvider.OPEN_METEO).build();
    Mockito.when(providerService.getAllEnabledOrderedByPriority())
        .thenReturn(List.of(wp));
    Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
        .thenReturn(Map.of("0", new WeatherCondition()));
    Mockito.when(proxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);

    assertThat(resolver.getForecastApiProvider()).isEqualTo(ForecastApiProvider.OPEN_METEO);
  }

  @Test
  @DisplayName("Should return map of actual weather condition provider code and related object")
  void getWithActualProvider_returnsWeatherConditionMap() {
    WeatherCondition condition = new WeatherCondition();
    WeatherProvider wp = WeatherProvider.builder()
        .provider(ForecastApiProvider.OPEN_METEO).build();
    Mockito.when(providerService.getAllEnabledOrderedByPriority())
        .thenReturn(List.of(wp));
    Mockito.when(conditionService.getAllByProvider(ForecastApiProvider.OPEN_METEO))
        .thenReturn(Map.of("0", condition));
    Mockito.when(proxy.getType()).thenReturn(ForecastApiProvider.OPEN_METEO);
    assertThat(resolver.getWithActualProvider()).isEqualTo(Map.of("0", condition));
  }
}
