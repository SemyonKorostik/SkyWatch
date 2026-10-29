package dev.korostik.skywatch.client;

import dev.korostik.skywatch.dto.weather.external.feignclient.ForecastResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * https://api.open-meteo.com/v1/forecast?latitude=53.9&longitude=27.55&hourly=temperature_2m,relative_humidity_2m,weather_code,apparent_temperature,precipitation,precipitation_probability&timezone=c&forecast_days=1
 * https://api.open-meteo.com/v1/forecast?latitude=53.9&longitude=27.55&daily=temperature_2m_max,temperature_2m_min,weather_code&timezone=Europe%2FMoscow&forecast_days=1
 * https://api.open-meteo.com/v1/forecast?latitude=53.9&longitude=27.55&current=temperature_2m,relative_humidity_2m,weather_code,apparent_temperature,wind_speed_10m,wind_direction_10m,surface_pressure&timezone=Europe%2FMoscow
 */

@FeignClient(name = "openMeteoApi", url = "https://api.open-meteo.com")
public interface OpenMeteoApiClient {

  @GetMapping("/v1/forecast")
  ForecastResponse getForecast(
      @RequestParam("latitude") Double latitude,
      @RequestParam("longitude") Double longitude,
      @RequestParam(value = "hourly", required = false) List<String> hourly,
      @RequestParam(value = "daily", required = false) List<String> daily,
      @RequestParam(value = "current", required = false) List<String> current,
      @RequestParam(value = "timezone", required = false) String timezone,
      @RequestParam(value = "forecast_days", required = false) Integer forecastDays
  );
}
