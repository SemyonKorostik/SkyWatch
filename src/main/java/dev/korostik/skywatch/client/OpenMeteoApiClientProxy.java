package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenMeteoApiClientProxy implements ForecastApiClientProxy{

  private final OpenMeteoApiClient openMeteoApiClient;

  @Override
  public ForecastApiProvider type() {
    return ForecastApiProvider.OPEN_METEO;
  }

  public ForecastResponse getForecast(ForecastRequest request) {
    return openMeteoApiClient.getForecast(request.latitude(),
            request.longitude(), request.hourly(), request.daily(), request.current(), request.timezone(), request.forecastDays());
  }
}
