package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClientProxy {

  private final OpenWeatherApiClient openWeatherApiClient;

  public ForecastResponse getForecast(ForecastRequest request) {
    return openWeatherApiClient.getForecast(request);
  }
}
