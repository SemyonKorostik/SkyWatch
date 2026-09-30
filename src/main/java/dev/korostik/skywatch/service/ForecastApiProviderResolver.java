package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastApiProviderResolver {

  private final WeatherProviderService weatherProviderService;
  private final Map<ForecastApiProvider, ForecastApiClientProxy> forecastApiClientProxyMap;
  private final WeatherConditionService weatherConditionService;

  public ForecastApiProvider getForecastApiProvider() {
    return weatherProviderService.getAllEnabled()
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
