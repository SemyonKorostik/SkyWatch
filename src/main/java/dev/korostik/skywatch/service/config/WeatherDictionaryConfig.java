package dev.korostik.skywatch.service.config;

import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
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

  @Bean
  public Map<String, WeatherCondition> weatherConditionOpenMeteoDictionary() {
    return weatherConditionRepository.findAllByProviderName(
            weatherProviderRepository.findByName("open-meteo").orElseThrow())
        .stream()
        .collect(Collectors.toUnmodifiableMap(WeatherCondition::getProviderCode,
            weatherCondition -> weatherCondition));
  }
}

