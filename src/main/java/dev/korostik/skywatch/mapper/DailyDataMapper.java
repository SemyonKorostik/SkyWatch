package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.DailyData;
import dev.korostik.skywatch.dto.weather.DailyJsonData;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DailyDataMapper {

  public List<DailyData> toList(DailyJsonData jsonData) {
    return IntStream.range(0, jsonData.time().size())
        .mapToObj(i -> DailyData.builder()
            .temperatureMax(jsonData.temperatureMax().get(i))
            .temperatureMin(jsonData.temperatureMin().get(i))
            .weatherCode(jsonData.weatherCode().get(i))
            .time(jsonData.time().get(i))
            .build())
        .toList();
  }

}
