package dev.korostik.skywatch.service;

import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherConditionService {

  private final WeatherProviderService weatherProviderService;
  private final WeatherConditionRepository weatherConditionRepository;

  public WeatherCondition getByCode(ForecastApiProvider provider, String providerCode) {
    WeatherProvider weatherProvider = weatherProviderService.getByProvider(provider);
    return weatherConditionRepository.findByProviderNameAndProviderCode(weatherProvider, providerCode)
        .orElseThrow();
  }

  public Map<String, WeatherCondition> getAllByProvider(ForecastApiProvider provider) {
    return weatherConditionRepository.findAllByProviderName(
            weatherProviderService.getByProvider(provider)).stream()
        .collect(Collectors.toUnmodifiableMap(WeatherCondition::getProviderCode, x -> x));
  }

}
