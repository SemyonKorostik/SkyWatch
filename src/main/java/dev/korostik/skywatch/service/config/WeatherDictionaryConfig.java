package dev.korostik.skywatch.service.config;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WeatherDictionaryConfig {

  private final WeatherConditionRepository weatherConditionRepository;
  private final WeatherProviderRepository weatherProviderRepository;
  private final List<WeatherProvider> weatherProviders;
  private final List<ForecastApiClientProxy> forecastApiClientProxies;

  @Bean
  public Map<String, WeatherCondition> weatherConditionDictionary() {
    return weatherConditionRepository.findAllByProviderName(
            weatherProviderRepository.findByName(
                    ForecastApiClientProxy.stream()
                        .filter(WeatherProvider::getIsEnabled)
                        .map(WeatherProvider::getName)
                        .findFirst()
                        .orElseThrow())
                .orElseThrow())
        .stream()
        .collect(Collectors.toUnmodifiableMap(WeatherCondition::getProviderCode,
            weatherCondition -> weatherCondition));
  }

  public ForecastApiClientProxy getApiClient() {
    List<String> providerNames = weatherProviderRepository.findAllByIsEnabledOrderByPriorityAsc(
        true).stream().map(WeatherProvider::getName).toList();
    return forecastApiClientProxies.stream()
        .filter(x -> providerNames.contains(x.type().getName()))
        .findFirst()
        .orElseThrow();
  }
}

