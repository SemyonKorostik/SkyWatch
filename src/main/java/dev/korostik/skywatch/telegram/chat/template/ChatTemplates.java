package dev.korostik.skywatch.telegram.chat.template;

public class ChatTemplates {

  public static final String CURRENT_WEATHER_TEMPLATE = """
      🌍 *Current Weather — {location}*
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      
      {icon} *{temp}°C* — {condition}
      🌡️ Feels like: *{feelsLike}°C*
      📊 Min / Max: *{tempMin}°C* / *{tempMax}°C*
      
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      💧 Humidity:      *{humidity}%*
      💨 Wind:          *{windSpeed} km/h {windDir}*
      🧭 Pressure:      *{pressure} hPa*
      👁️ Visibility:    *{visibility} km*
      ☀️ UV Index:      *{uvIndex}* ({uvDesc})
      🌅 Sunrise:       *{sunrise}*
      🌇 Sunset:        *{sunset}*
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      🕐 Updated: {time}
      """;

  public static final String HOURLY_WEATHER_TEMPLATE = """
      ⏰ *Hourly Forecast — {location}*
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      
      {hourlyRows}
      
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      🕐 Updated: {time}
      """;

  public static final String HOURLY_WEATHER_ROW_TEMPLATE = """
      {icon} *{time}* — {temp}°C | 💧 {precip}% | 💨 {windSpeed} km/h
      """;

  public static final String DAILY_WEATHER_TEMPLATE = """
      📅 *{days}-Day Forecast — {location}*
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      
      {dailyRows}
      
      ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
      🕐 Updated: {time}
      """;

  public static final String DAILY_WEATHER_ROW_TEMPLATE = """
      *{dayName}* ({date})
      {icon} {condition}
      🌡️ {tempMax}° / {tempMin}°   💧 {precip}%   💨 {windSpeed} km/h
      """;
}
