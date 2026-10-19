package dev.korostik.skywatch.dto.weather.transit;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record HourlyData(
    LocalDateTime time,
    Double temperature,
    Integer relativeHumidity,
    String weatherCode,
    Double apparentTemperature,
    Double precipitation,
    Integer precipitationProbability,
    Double windSpeed,
    Integer windDirection,
    Double surfacePressure
) {

}
