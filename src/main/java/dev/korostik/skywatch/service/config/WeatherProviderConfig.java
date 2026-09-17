package dev.korostik.skywatch.service.config;

import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WeatherProviderConfig {

  private final WeatherProviderRepository weatherProviderRepository;

  @Bean
  public List<WeatherProvider> weatherProviders() {
    return weatherProviderRepository.findAllByIsEnabledOrderByPriorityAsc(Boolean.TRUE);
  }

}
