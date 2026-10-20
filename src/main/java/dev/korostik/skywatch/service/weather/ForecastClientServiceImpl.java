package dev.korostik.skywatch.service.weather;

import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.Location;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WmoWeatherCode;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForecastClientServiceImpl implements ForecastClientService {

  private final ForecastService forecastService;
  private final LocationClientService locationClientService;

  @Override
  public String getCurrentWeather(Location location, Language language) {
    WeatherSummary weatherSummary = forecastService.fetchAndSave(location,
        List.of(OpenWeatherParameters.CURRENT), null);

    LocationName locationName = locationClientService.findOrCreateLocationName(
        location.getLatitude(), location.getLongitude(), language);

    CurrentWeather current = weatherSummary.getCurrentWeather();
    if (current == null) {
      return "Weather data unavailable";
    }

    WeatherCondition condition = current.getWeatherCondition();
    WmoWeatherCode wmoCode = condition != null ? condition.getWmoCode() : null;
    String icon = wmoCode != null ? wmoCode.getEmoji() : "❓";
    String conditionText = wmoCode != null ? wmoCode.getDescription() : "Unknown";

    ZoneId zoneId = location.getTimeZoneId();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId);
    String updatedTime = current.getUpdatedAt() != null ? timeFormatter.format(current.getUpdatedAt()) : "N/A";

    String windDir = degreesToCompass(current.getWindDirection());
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
    WeatherSummary weatherSummary = forecastService.fetchAndSave(location,
        List.of(OpenWeatherParameters.CURRENT, OpenWeatherParameters.HOURLY), 2);

    LocationName locationName = locationClientService.findOrCreateLocationName(
        location.getLatitude(), location.getLongitude(), language);

    List<HourlyWeather> hourlyList = weatherSummary.getHourlyWeather();
    if (hourlyList == null || hourlyList.isEmpty()) {
      return "Hourly forecast unavailable";
    }

    ZoneId zoneId = location.getTimeZoneId();
    DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId);
    String updatedTime = weatherSummary.getCurrentWeather() != null && weatherSummary.getCurrentWeather().getUpdatedAt() != null
        ? hourFormatter.format(weatherSummary.getCurrentWeather().getUpdatedAt())
        : "N/A";

    Instant now = Instant.now();
    String hourlyRows = hourlyList.stream()
        .filter(hw -> hw.getId().getTime().isAfter(now.minusSeconds(1)))
        .limit(24)
        .map(hw -> formatHourlyRow(hw, zoneId, hourFormatter))
        .collect(Collectors.joining("\n"));

    Map<String, String> values = Map.of(
        "location", locationName.getPlaceName() + ", " + locationName.getCountry(),
        "hourlyRows", hourlyRows.isEmpty() ? "No data" : hourlyRows,
        "time", updatedTime
    );

    return TemplateEngine.render(ChatTemplates.HOURLY_WEATHER_TEMPLATE, values);
  }

  @Override
  public String getDailyForecast(Location location, Language language) {
    return renderDailyForecast(location, language, 3);
  }

  @Override
  public String getWeeklyForecast(Location location, Language language) {
    return renderDailyForecast(location, language, 7);
  }

  private String renderDailyForecast(Location location, Language language, int days) {
    WeatherSummary weatherSummary = forecastService.fetchAndSave(location,
        List.of(OpenWeatherParameters.CURRENT, OpenWeatherParameters.DAILY), days);

    LocationName locationName = locationClientService.findOrCreateLocationName(
        location.getLatitude(), location.getLongitude(), language);

    List<DailyWeather> dailyList = weatherSummary.getDailyWeather();
    if (dailyList == null || dailyList.isEmpty()) {
      return "Daily forecast unavailable";
    }

    ZoneId zoneId = location.getTimeZoneId();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM").withZone(zoneId);
    Locale locale = new Locale(language.getCode());
    DateTimeFormatter dayNameFormatter = DateTimeFormatter.ofPattern("EEEE").withZone(zoneId).withLocale(locale);
    String updatedTime = weatherSummary.getCurrentWeather() != null && weatherSummary.getCurrentWeather().getUpdatedAt() != null
        ? DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId).format(weatherSummary.getCurrentWeather().getUpdatedAt())
        : "N/A";

    String dailyRows = dailyList.stream()
        .limit(days)
        .map(dw -> formatDailyRow(dw, zoneId, dateFormatter, dayNameFormatter))
        .collect(Collectors.joining("\n\n"));

    Map<String, String> values = Map.of(
        "location", locationName.getPlaceName() + ", " + locationName.getCountry(),
        "days", String.valueOf(days),
        "dailyRows", dailyRows.isEmpty() ? "No data" : dailyRows,
        "time", updatedTime
    );

    return TemplateEngine.render(ChatTemplates.DAILY_WEATHER_TEMPLATE, values);
  }

  private String formatHourlyRow(HourlyWeather hw, ZoneId zoneId, DateTimeFormatter timeFormatter) {
    Instant timeInstant = hw.getId().getTime();
    String timeStr = timeFormatter.format(timeInstant);

    WmoWeatherCode wmoCode = getWmoCode(hw.getWeatherCondition());
    String icon = wmoCode != null ? wmoCode.getEmoji() : "❓";
    String tempStr = formatDouble(hw.getTemperature());
    String precipStr = formatInt(hw.getPrecipitationProbability());
    Double windSpeedKmh = hw.getWindSpeed() != null ? hw.getWindSpeed() * 3.6 : 0.0;
    String windStr = String.format(Locale.US, "%.1f", windSpeedKmh);

    return TemplateEngine.render(ChatTemplates.HOURLY_WEATHER_ROW_TEMPLATE, Map.of(
        "icon", icon,
        "time", timeStr,
        "temp", tempStr,
        "precip", precipStr,
        "windSpeed", windStr
    ));
  }

  private String formatDailyRow(DailyWeather dw, ZoneId zoneId, DateTimeFormatter dateFormatter, DateTimeFormatter dayNameFormatter) {
    Instant dateInstant = dw.getId().getDate();
    String dateStr = dateFormatter.format(dateInstant);
    String dayNameStr = dayNameFormatter.format(dateInstant);

    WmoWeatherCode wmoCode = getWmoCode(dw.getWeatherCondition());
    String icon = wmoCode != null ? wmoCode.getEmoji() : "❓";
    String conditionText = wmoCode != null ? wmoCode.getDescription() : "Unknown";

    String tempMaxStr = formatDouble(dw.getTemperatureMax());
    String tempMinStr = formatDouble(dw.getTemperatureMin());
    
    // DailyWeather entity lacks precipitation fields. Using humidity as fallback for {precip}% placeholder.
    String precipStr = formatInt(dw.getHumidity());
    
    Double windSpeedKmh = dw.getWindSpeed() != null ? dw.getWindSpeed() * 3.6 : 0.0;
    String windStr = String.format(Locale.US, "%.1f", windSpeedKmh);

    return TemplateEngine.render(ChatTemplates.DAILY_WEATHER_ROW_TEMPLATE, Map.of(
        "dayName", dayNameStr,
        "date", dateStr,
        "icon", icon,
        "condition", conditionText,
        "tempMax", tempMaxStr,
        "tempMin", tempMinStr,
        "precip", precipStr,
        "windSpeed", windStr
    ));
  }

  private WmoWeatherCode getWmoCode(WeatherCondition condition) {
    return condition != null ? condition.getWmoCode() : null;
  }

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
