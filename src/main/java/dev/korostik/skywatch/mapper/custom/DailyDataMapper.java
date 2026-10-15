package dev.korostik.skywatch.mapper.custom;

import dev.korostik.skywatch.dto.weather.external.DailyDataDto;
import dev.korostik.skywatch.dto.weather.transit.DailyData;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DailyDataMapper {

  public List<DailyData> tolist(DailyDataDto jsonData) {
    return IntStream.range(0, jsonData.time().size())
        .mapToObj(i -> DailyData.builder()
            .temperatureMax(jsonData.temperatureMax().get(i))
            .temperatureMin(jsonData.temperatureMin().get(i))
            .weatherCode(jsonData.weatherCode().get(i))
            .date(jsonData.time().get(i))
            .build())
        .toList();
  }

  public DailyDataDto toDto(List<DailyData> dailyData) {
    return DailyDataDto.builder()
        .time(dailyData.stream().map(DailyData::date).toList())
        .temperatureMax(dailyData.stream().map(DailyData::temperatureMax).toList())
        .temperatureMin(dailyData.stream().map(DailyData::temperatureMin).toList())
        .weatherCode(dailyData.stream().map(DailyData::weatherCode).toList())
        .build();
  }

}
