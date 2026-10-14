package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.external.HourlyDataDto;
import dev.korostik.skywatch.dto.weather.transit.HourlyData;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class HourlyDataMapper {

  public List<HourlyData> toList(HourlyDataDto jsonData) {
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

  public HourlyDataDto toDto(List<HourlyData> hourlyData) {
    return HourlyDataDto.builder()
        .time(hourlyData.stream().map(HourlyData::time).collect(Collectors.toList()))
        .apparentTemperature(
            hourlyData.stream().map(HourlyData::apparentTemperature).collect(Collectors.toList()))
        .precipitation(
            hourlyData.stream().map(HourlyData::precipitation).collect(Collectors.toList()))
        .temperature(hourlyData.stream().map(HourlyData::temperature).collect(Collectors.toList()))
        .windDirection(
            hourlyData.stream().map(HourlyData::windDirection).collect(Collectors.toList()))
        .windSpeed(hourlyData.stream().map(HourlyData::windSpeed).collect(Collectors.toList()))
        .weatherCode(hourlyData.stream().map(HourlyData::weatherCode).collect(Collectors.toList()))
        .precipitationProbability(hourlyData.stream().map(HourlyData::precipitationProbability)
            .collect(Collectors.toList()))
        .relativeHumidity(
            hourlyData.stream().map(HourlyData::relativeHumidity).collect(Collectors.toList()))
        .surfacePressure(
            hourlyData.stream().map(HourlyData::surfacePressure).collect(Collectors.toList()))
        .build();
  }

}
