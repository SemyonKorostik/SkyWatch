package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.external.ForecastRequest;
import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;

public interface ForecastApiClientProxy {

  ForecastApiProvider getType();

  ForecastResponse getForecast(ForecastRequest request);
}
