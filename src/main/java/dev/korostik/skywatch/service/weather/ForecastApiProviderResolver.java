package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ForecastApiProviderResolver {

  private final WeatherProviderService weatherProviderService;
  private final Map<ForecastApiProvider, ForecastApiClientProxy> forecastApiClientProxyMap;
  private final WeatherConditionService weatherConditionService;

  public ForecastApiProvider getForecastApiProvider() {
    return weatherProviderService.getAllEnabledOrderedByPriority()
        .stream()
        .map(WeatherProvider::getProvider)
        .filter(forecastApiClientProxyMap::containsKey)
        .filter(x -> !weatherConditionService.getAllByProvider(x).isEmpty())
        .findFirst()
        .orElseThrow();
  }

  public Map<String, WeatherCondition> getWithActualProvider() {
    return weatherConditionService.getAllByProvider(getForecastApiProvider());
  }

}
