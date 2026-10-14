package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.Map;

public interface WeatherConditionService {

  Map<String, WeatherCondition> getAllByProvider(ForecastApiProvider provider);
}
