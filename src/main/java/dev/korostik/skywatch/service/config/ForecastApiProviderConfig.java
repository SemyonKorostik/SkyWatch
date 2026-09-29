package dev.korostik.skywatch.service.config;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ForecastApiProviderConfig {

  private final List<ForecastApiClientProxy> forecastApiClientProxies;

  @Bean
  public Map<ForecastApiProvider, ForecastApiClientProxy> forecastApiClientProxies() {
    return forecastApiClientProxies.stream().collect(Collectors.toUnmodifiableMap(
        ForecastApiClientProxy::getType, x -> x
    ));
  }

}
