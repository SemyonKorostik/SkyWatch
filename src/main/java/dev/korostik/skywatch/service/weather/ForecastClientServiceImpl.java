package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WmoWeatherCode;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.service.location.LocationClientService;
import dev.korostik.skywatch.telegram.chat.template.ChatTemplates;
import dev.korostik.skywatch.telegram.chat.template.TemplateEngine;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastClientServiceImpl implements ForecastClientService {

  private final ForecastService forecastService;
  private final LocationClientService locationClientService;

  @Override
  public String getCurrentWeather(Location location, Language language) {
    // Fetch current weather data
    WeatherSummary weatherSummary = forecastService.fetchAndSave(location,
        List.of(OpenWeatherParameters.CURRENT), null);

    LocationName locationName = locationClientService.findOrCreateLocationName(
        location.getLatitude(), location.getLongitude(), language);

    CurrentWeather current = weatherSummary.getCurrentWeather();
    if (current == null) {
      return "Weather data unavailable";
    }

    // Resolve WMO code details (icon, description)
    WeatherCondition condition = current.getWeatherCondition();
    WmoWeatherCode wmoCode = condition != null ? condition.getWmoCode() : null;
    String icon = wmoCode != null ? wmoCode.getEmoji() : "❓";
    String conditionText = wmoCode != null ? wmoCode.getDescription() : "Unknown";

    // Format time in location's timezone
    ZoneId zoneId = location.getTimeZoneId();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId);
    String updatedTime = current.getUpdatedAt() != null ? timeFormatter.format(current.getUpdatedAt()) : "N/A";

    // Wind direction helper
    String windDir = degreesToCompass(current.getWindDirection());

    // Wind speed conversion m/s -> km/h (OpenMeteo default is m/s)
    Double windSpeedKmh = current.getWindSpeed() != null ? current.getWindSpeed() * 3.6 : 0.0;

    String locationParam = locationName.getPlaceName() + ", " + locationName.getCountry();

    Map<String, String> values = Map.ofEntries(
        Map.entry("location", locationParam),
        Map.entry("icon", icon),
        Map.entry("temp", formatDouble(current.getTemperature())),
        Map.entry("condition", conditionText),
        Map.entry("humidity", formatInt(current.getHumidity())),
        Map.entry("windSpeed", String.format(Locale.US, "%.1f", windSpeedKmh)),
        Map.entry("windDir", windDir),
        Map.entry("pressure", formatDouble(current.getPressure())),
        Map.entry("time", updatedTime)
    );

    return TemplateEngine.render(ChatTemplates.CURRENT_WEATHER_TEMPLATE, values);
  }

  @Override
  public String getHourlyForecast(Location location, Language language) {
    return "";
  }

  @Override
  public String getDailyForecast(Location location, Language language) {
    return "";
  }

  @Override
  public String getWeeklyForecast(Location location, Language language) {
    return "";
  }

  // --- Helpers ---

  private String formatDouble(Double value) {
    return value != null ? String.format(Locale.US, "%.1f", value) : "N/A";
  }

  private String formatInt(Integer value) {
    return value != null ? value.toString() : "N/A";
  }

  private String degreesToCompass(Integer degrees) {
    if (degrees == null) return "";
    String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
    int index = (int) Math.round(degrees / 22.5) % 16;
    return directions[index < 0 ? index + 16 : index];
  }
}
