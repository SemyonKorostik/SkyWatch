package dev.korostik.skywatch.dto.weather;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ForecastResponse(
    @JsonProperty("latitude") Float latitude,
    @JsonProperty("longitude") Float longitude,
    @JsonProperty("generationtime_ms") Double generationTimeMs,
    @JsonProperty("utc_offset_seconds") Integer utcOffsetSeconds,
    @JsonProperty("timezone") String timezone,
    @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
    @JsonProperty("elevation") Integer elevation,
    @JsonProperty("hourly_units") HourlyUnits hourlyUnits,
    @JsonProperty("hourly") HourlyData hourly,
    @JsonProperty("daily_units") DailyUnits dailyUnits,
    @JsonProperty("daily") DailyData daily,
    @JsonProperty("current_units") CurrentUnits currentUnits,
    @JsonProperty("current") CurrentData current
) {

}
