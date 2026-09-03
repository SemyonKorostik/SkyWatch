package dev.korostik.skywatch.dto.weather;

import java.util.List;
import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
public record ForecastRequest(
    @RequestParam(value = "latitude") Double latitude,
    @RequestParam(value = "longitude") Double longitude,
    @RequestParam(value = "hourly", required = false) List<String> hourly,
    @RequestParam(value = "daily", required = false) List<String> daily,
    @RequestParam(value = "current", required = false) List<String> current,
    @RequestParam(value = "timezone", required = false) String timezone,
    @RequestParam(value = "forecast_days", required = false) Integer forecastDays
) {

}
