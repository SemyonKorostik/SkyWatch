package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.external.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class OpenMeteoApiAdapter implements ForecastApiClientProxy {

  private final OpenMeteoApiClient openMeteoApiClient;

  @Override
  public ForecastApiProvider getType() {
    return ForecastApiProvider.OPEN_METEO;
  }

  public @Valid ForecastResponse getForecast(ForecastRequest request) {
    return openMeteoApiClient.getForecast(request.latitude(),
        request.longitude(), request.hourly(), request.daily(), request.current(), request.zoneId(),
        request.forecastDays());
  }
}
