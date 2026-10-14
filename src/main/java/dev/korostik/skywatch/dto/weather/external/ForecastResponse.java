package dev.korostik.skywatch.dto.weather.external;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZoneId;
import org.hibernate.validator.constraints.Range;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastResponse(
    @JsonProperty("latitude") @NotNull @Range(min = -90, max = 90) Double latitude,
    @JsonProperty("longitude") @NotNull @Range(min = -180, max = 180) Double longitude,
    @JsonProperty("timezone") @NotNull ZoneId zoneId,
    @JsonProperty("hourly_units") @Valid HourlyUnits hourlyUnits,
    @JsonProperty("hourly") @Valid HourlyDataDto hourly,
    @JsonProperty("daily_units") @Valid DailyUnits dailyUnits,
    @JsonProperty("daily") @Valid DailyDataDto daily,
    @JsonProperty("current_units") @Valid CurrentUnits currentUnits,
    @JsonProperty("current") @Valid CurrentDataDto current
) {

}
