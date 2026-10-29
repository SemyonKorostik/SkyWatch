package dev.korostik.skywatch.dto.weather.external.response;

import dev.korostik.skywatch.dto.location.LocationNameDto;
import java.util.List;

public record WeatherSummaryDto(CurrentWeatherDto current,
                                List<HourlyForecastDto> hourly,
                                List<DailyForecastDto> daily,
                                LocationNameDto locationName
) {

}
