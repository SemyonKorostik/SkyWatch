package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClientProxy {

  private final OpenMeteoApiClient openMeteoApiClient;

  public ForecastResponse getForecast(ForecastRequest request) {
    return openMeteoApiClient.getForecast(request);
  }
}
