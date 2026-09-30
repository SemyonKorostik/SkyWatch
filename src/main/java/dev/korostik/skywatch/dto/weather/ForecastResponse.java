package dev.korostik.skywatch.dto.weather;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import feign.Param;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastResponse(
    @JsonProperty("latitude") Double latitude,
    @JsonProperty("longitude") Double longitude,
    @JsonProperty("generationtime_ms") Long generationTimeMs,
    @JsonProperty("utc_offset_seconds") Integer utcOffsetSeconds,
    @JsonProperty("timezone") ZoneId zoneId,
    @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
    @JsonProperty("elevation") Integer elevation,
    @JsonProperty("hourly_units") HourlyUnits hourlyUnits,
    @JsonProperty("hourly") HourlyJsonData hourly,
    @JsonProperty("daily_units") DailyUnits dailyUnits,
    @JsonProperty("daily") DailyJsonData daily,
    @JsonProperty("current_units") CurrentUnits currentUnits,
    @JsonProperty("current") CurrentData current
) {

}
