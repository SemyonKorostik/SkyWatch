package dev.korostik.skywatch.service;

import dev.korostik.skywatch.client.ForecastApiClientProxy;
import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherProviderService {

  private final WeatherProviderRepository weatherProviderRepository;
  private final Map<ForecastApiProvider, ForecastApiClientProxy> forecastApiClientProxies;

  public WeatherProvider getByProvider(ForecastApiProvider provider) {
    return weatherProviderRepository.findByProvider(provider).orElseThrow();
  }

  public List<WeatherProvider> getAllEnabled() {
    return weatherProviderRepository.findAllByIsEnabledOrderByPriorityAsc(true);
  }


}
