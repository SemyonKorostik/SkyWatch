package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.ForecastRequest;
import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;

public interface ForecastApiClientProxy {

  ForecastApiProvider getType();

  ForecastResponse getForecast(ForecastRequest request);
}
