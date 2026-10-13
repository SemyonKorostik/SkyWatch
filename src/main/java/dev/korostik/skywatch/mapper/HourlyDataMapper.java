package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.HourlyData;
import dev.korostik.skywatch.dto.weather.HourlyJsonData;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class HourlyDataMapper {

  public List<HourlyData> toList(HourlyJsonData jsonData) {
    return IntStream.range(0, jsonData.time().size())
        .mapToObj(i -> HourlyData.builder()
            .apparentTemperature(jsonData.apparentTemperature().get(i))
            .precipitation(jsonData.precipitation().get(i))
            .temperature(jsonData.temperature().get(i))
            .precipitationProbability(jsonData.precipitationProbability().get(i))
            .relativeHumidity(jsonData.relativeHumidity().get(i))
            .surfacePressure(jsonData.surfacePressure().get(i))
            .time(jsonData.time().get(i))
            .weatherCode(jsonData.weatherCode().get(i))
            .windDirection(jsonData.windDirection().get(i))
            .windSpeed(jsonData.windSpeed().get(i))
            .build())
        .toList();
  }

}
