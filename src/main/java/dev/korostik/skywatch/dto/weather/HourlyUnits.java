package dev.korostik.skywatch.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HourlyUnits(
    @JsonProperty("time") String time,
    @JsonProperty("temperature_2m") String temperature2m,
    @JsonProperty("relative_humidity_2m") String relativeHumidity2m,
    @JsonProperty("weather_code") String weatherCode,
    @JsonProperty("apparent_temperature") String apparentTemperature,
    @JsonProperty("precipitation") String precipitation,
    @JsonProperty("precipitation_probability") String precipitationProbability

) {}
