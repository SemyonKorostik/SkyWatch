package dev.korostik.skywatch.dto.weather.domain;

import dev.korostik.skywatch.dto.weather.external.ForecastResponse;
import dev.korostik.skywatch.enums.ForecastApiProvider;

public record ProviderResult(ForecastApiProvider provider, ForecastResponse response) {

}
