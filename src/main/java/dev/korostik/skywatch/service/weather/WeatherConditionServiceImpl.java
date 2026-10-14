package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import dev.korostik.skywatch.repository.WeatherConditionRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherConditionServiceImpl implements WeatherConditionService {

  private final WeatherProviderService weatherProviderService;
  private final WeatherConditionRepository weatherConditionRepository;

  @Override
  public Map<String, WeatherCondition> getAllByProvider(ForecastApiProvider provider) {
    return weatherConditionRepository.findAllByProviderName(
            weatherProviderService.getByProvider(provider)).stream()
        .collect(Collectors.toUnmodifiableMap(WeatherCondition::getProviderCode, x -> x));
  }

}
