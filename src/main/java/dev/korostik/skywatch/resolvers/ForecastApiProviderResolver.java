package dev.korostik.skywatch.resolvers;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entities.WeatherCondition;
import dev.korostik.skywatch.entities.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.service.weather.WeatherConditionService;
import dev.korostik.skywatch.service.weather.WeatherProviderService;
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
