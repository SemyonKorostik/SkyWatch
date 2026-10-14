package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.repository.WeatherProviderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherProviderServiceImpl implements WeatherProviderService {

  private final WeatherProviderRepository weatherProviderRepository;

  @Override
  public WeatherProvider getByProvider(ForecastApiProvider provider) {
    return weatherProviderRepository.findByProvider(provider).orElseThrow();
  }

  @Override
  public List<WeatherProvider> getAllEnabledOrderedByPriority() {
    return weatherProviderRepository.findAllByIsEnabledOrderByPriorityAsc(true);
  }


}
