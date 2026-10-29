package dev.korostik.skywatch.service.weather;

import com.pengrad.telegrambot.request.SendMessage;
import dev.korostik.skywatch.dto.weather.external.response.CurrentWeatherDto;
import dev.korostik.skywatch.dto.weather.external.response.DailyForecastDto;
import dev.korostik.skywatch.dto.weather.external.response.HourlyForecastDto;
import dev.korostik.skywatch.dto.weather.external.response.WeatherConditionDto;
import dev.korostik.skywatch.dto.weather.external.response.WeatherSummaryDto;
import dev.korostik.skywatch.dto.weather.external.response.WmoWeatherCodeDto;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.enums.OpenWeatherParameters;
import dev.korostik.skywatch.telegram.chat.template.ChatTemplates;
import dev.korostik.skywatch.telegram.chat.template.TemplateEngine;
import dev.korostik.skywatch.telegram.menu.KeyboardMenu;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
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
  private final TelegramBotExecutor telegramBotExecutor;
  private final KeyboardMenu keyboardMenu;

  @Override
  public void sendCurrentWeather(Long chatId, Long locationId, Language language) {
    WeatherSummaryDto weatherSummaryDto = forecastService.fetchAndSave(locationId,
        List.of(OpenWeatherParameters.CURRENT), null, language);

    CurrentWeatherDto current = weatherSummaryDto.current();

    WeatherConditionDto condition = current.weatherCondition();
    WmoWeatherCodeDto wmoCode = condition != null ? condition.wmoCode() : null;
    String icon = wmoCode != null ? wmoCode.emoji() : "❓";
    String conditionText = wmoCode != null ? wmoCode.description() : "Unknown";
    String windDir = degreesToCompass(current.windDirection());
    Double windSpeedKmh = current.windSpeed() != null ? current.windSpeed() * 3.6 : 0.0;
    ZoneId zoneId = weatherSummaryDto.locationName().timeZoneId();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId);
    String updatedTime =
        current.updatedAt() != null ? timeFormatter.format(current.updatedAt()) : "N/A";
    String locationParam =
        weatherSummaryDto.locationName().placeName() + ", " + weatherSummaryDto.locationName()
            .country();

    Map<String, String> values = Map.ofEntries(
        Map.entry("location", locationParam),
        Map.entry("icon", icon),
        Map.entry("temp", formatDouble(current.temperature())),
        Map.entry("condition", conditionText),
        Map.entry("humidity", formatInt(current.humidity())),
        Map.entry("windSpeed", String.format(Locale.US, "%.1f", windSpeedKmh)),
        Map.entry("windDir", windDir),
        Map.entry("pressure", formatDouble(current.pressure())),
        Map.entry("time", updatedTime)
    );

    String render = TemplateEngine.render(ChatTemplates.CURRENT_WEATHER_TEMPLATE, values);
    SendMessage message = new SendMessage(chatId, render);
    message.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    telegramBotExecutor.execute(message);
  }

  @Override
  public void sendHourlyForecast(Long chatId, Long locationId, Language language) {
    WeatherSummaryDto weatherSummaryDto = forecastService.fetchAndSave(locationId,
        List.of(OpenWeatherParameters.CURRENT, OpenWeatherParameters.HOURLY), 2, language);

    List<HourlyForecastDto> hourlyList = weatherSummaryDto.hourly();

    ZoneId zoneId = weatherSummaryDto.locationName().timeZoneId();
    DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId);
    String updatedTime = weatherSummaryDto.current() != null
        && weatherSummaryDto.current().updatedAt() != null
        ? hourFormatter.format(weatherSummaryDto.current().updatedAt())
        : "N/A";

    Instant now = Instant.now();
    String hourlyRows = hourlyList.stream()
        .filter(hw -> hw.time().isAfter(now.minusSeconds(1)))
        .limit(24)
        .map(hw -> formatHourlyRow(hw, hourFormatter))
        .collect(Collectors.joining("\n"));

    Map<String, String> values = Map.of(
        "location",
        weatherSummaryDto.locationName().placeName() + ", "
            + weatherSummaryDto.locationName()
            .country(),
        "hourlyRows", hourlyRows.isEmpty() ? "No data" : hourlyRows,
        "time", updatedTime
    );

    String render = TemplateEngine.render(ChatTemplates.HOURLY_WEATHER_TEMPLATE, values);
    SendMessage message = new SendMessage(chatId, render);
    message.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    telegramBotExecutor.execute(message);
  }

  @Override
  public void sendDailyForecast(Long chatId, Long locationId, Language language) {
    String render = renderDailyForecast(locationId, language, 3);
    SendMessage message = new SendMessage(chatId, render);
    message.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    telegramBotExecutor.execute(message);
  }

  @Override
  public void sendWeeklyForecast(Long chatId, Long locationId, Language language) {
    String render = renderDailyForecast(locationId, language, 7);
    SendMessage message = new SendMessage(chatId, render);
    message.setReplyMarkup(keyboardMenu.getForecastReplyMarkup());
    telegramBotExecutor.execute(message);
  }

  private String renderDailyForecast(Long locationId, Language language, int days) {
    WeatherSummaryDto weatherSummaryDto = forecastService.fetchAndSave(locationId,
        List.of(OpenWeatherParameters.CURRENT, OpenWeatherParameters.DAILY), days, language);

    List<DailyForecastDto> dailyList = weatherSummaryDto.daily();
    if (dailyList == null || dailyList.isEmpty()) {
      return "Daily forecast unavailable";
    }

    ZoneId zoneId = weatherSummaryDto.locationName().timeZoneId();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM").withZone(zoneId);
    DateTimeFormatter dayNameFormatter = DateTimeFormatter.ofPattern("EEEE").withZone(zoneId);
    String updatedTime = weatherSummaryDto.current() != null
        && weatherSummaryDto.current().updatedAt() != null
        ? DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId)
        .format(weatherSummaryDto.current().updatedAt())
        : "N/A";

    String dailyRows = dailyList.stream()
        .limit(days)
        .map(dw -> formatDailyRow(dw, dateFormatter, dayNameFormatter))
        .collect(Collectors.joining("\n\n"));

    Map<String, String> values = Map.of(
        "location",
        weatherSummaryDto.locationName().placeName() + ", " + weatherSummaryDto.locationName()
            .country(),
        "days", String.valueOf(days),
        "dailyRows", dailyRows.isEmpty() ? "No data" : dailyRows,
        "time", updatedTime
    );

    return TemplateEngine.render(ChatTemplates.DAILY_WEATHER_TEMPLATE, values);
  }

  private String formatHourlyRow(HourlyForecastDto hw, DateTimeFormatter timeFormatter) {
    Instant timeInstant = hw.time();
    String timeStr = timeFormatter.format(timeInstant);

    WmoWeatherCodeDto wmoCode = getWmoCode(hw.weatherCondition());
    String icon = wmoCode != null ? wmoCode.emoji() : "❓";
    String tempStr = formatDouble(hw.temperature());
    String precipStr = formatInt(hw.precipitationProbability());
    Double windSpeedKmh = hw.windSpeed() != null ? hw.windSpeed() * 3.6 : 0.0;
    String windStr = String.format(Locale.US, "%.1f", windSpeedKmh);

    return TemplateEngine.render(ChatTemplates.HOURLY_WEATHER_ROW_TEMPLATE, Map.of(
        "icon", icon,
        "time", timeStr,
        "temp", tempStr,
        "precip", precipStr,
        "windSpeed", windStr
    ));
  }

  private String formatDailyRow(DailyForecastDto dw, DateTimeFormatter dateFormatter,
      DateTimeFormatter dayNameFormatter) {
    Instant dateInstant = dw.date();
    String dateStr = dateFormatter.format(dateInstant);
    String dayNameStr = dayNameFormatter.format(dateInstant);

    WmoWeatherCodeDto wmoCode = getWmoCode(dw.weatherCondition());
    String icon = wmoCode != null ? wmoCode.emoji() : "❓";
    String conditionText = wmoCode != null ? wmoCode.description() : "Unknown";

    String tempMaxStr = formatDouble(dw.temperatureMax());
    String tempMinStr = formatDouble(dw.temperatureMin());

    // DailyWeather entity lacks precipitation fields. Using humidity as fallback for {precip}% placeholder.
    String precipStr = formatInt(dw.humidity());

    Double windSpeedKmh = dw.windSpeed() != null ? dw.windSpeed() * 3.6 : 0.0;
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

  private WmoWeatherCodeDto getWmoCode(WeatherConditionDto condition) {
    return condition != null ? condition.wmoCode() : null;
  }

  private String formatDouble(Double value) {
    return value != null ? String.format(Locale.US, "%.1f", value) : "N/A";
  }

  private String formatInt(Integer value) {
    return value != null ? value.toString() : "N/A";
  }

  private String degreesToCompass(Integer degrees) {
    if (degrees == null) {
      return "";
    }
    String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW",
        "WSW", "W", "WNW", "NW", "NNW"};
    int index = (int) Math.round(degrees / 22.5) % 16;
    return directions[index < 0 ? index + 16 : index];
  }
}
