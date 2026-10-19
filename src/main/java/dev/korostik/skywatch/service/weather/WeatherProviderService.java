package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.entities.reference.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import java.util.List;

public interface WeatherProviderService {

  WeatherProvider getByProvider(ForecastApiProvider provider);

  List<WeatherProvider> getAllEnabledOrderedByPriority();
}
