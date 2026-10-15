package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForecastApiProviderResolver {

  private final WeatherProviderService weatherProviderService;
  private final Map<ForecastApiProvider, ForecastApiClientProxy> forecastApiClientProxyMap;
  private final WeatherConditionService weatherConditionService;

  // Existing: used for mapping codes (Current/Daily/Hourly mappers need this)
  public Map<String, WeatherCondition> getWithActualProvider() {
    return weatherConditionService.getAllByProvider(getForecastApiProvider());
  }

  // Existing: returns the single highest priority provider (for non-fallback contexts if any)
  public ForecastApiProvider getForecastApiProvider() {
    return getAvailableProviders().stream()
        .findFirst()
        .map(ForecastApiClientProxy::getType)
        .orElseThrow(() -> new IllegalStateException("No available weather provider configured"));
  }

  // NEW: Returns ordered list of READY-TO-USE provider adapters (beans)
  // Encapsulates all validity checks: Enabled in DB -> Adapter exists -> Conditions mapped
  public List<ForecastApiClientProxy> getAvailableProviders() {
    return weatherProviderService.getAllEnabledOrderedByPriority().stream()
        .map(WeatherProvider::getProvider)
        .filter(forecastApiClientProxyMap::containsKey)
        .filter(providerType -> !weatherConditionService.getAllByProvider(providerType).isEmpty())
        .map(forecastApiClientProxyMap::get)
        .toList();
  }
}
