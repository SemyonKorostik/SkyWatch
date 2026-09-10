package dev.korostik.skywatch.dto.weather;

import java.util.List;

import feign.Param;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
public record ForecastRequest(
    @Param(value = "latitude") Double latitude,
    @Param(value = "longitude") Double longitude,
    @Param(value = "hourly") List<String> hourly,
    @Param(value = "daily") List<String> daily,
    @Param(value = "current") List<String> current,
    @Param(value = "timezone") String timezone,
    @Param(value = "forecast_days") Integer forecastDays
) {

}
