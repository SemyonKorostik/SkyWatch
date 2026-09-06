package dev.korostik.skywatch.mapper;

import dev.korostik.skywatch.dto.weather.ForecastResponse;
import dev.korostik.skywatch.entity.DailyWeather;
import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.service.LocationService;
import dev.korostik.skywatch.service.WeatherConditionService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class WeatherMapper {
    private final WeatherConditionService weatherConditionService;
    private final LocationService locationService;

    public List<DailyWeather> toEntity(ForecastResponse dto) {
        List<LocalDate> time = dto.daily().time();
        return IntStream.range(0, time.size())
                .mapToObj(i -> DailyWeather.builder()
                        .createdAt(Instant.ofEpochMilli(dto.generationTimeMs()))
                        .date(dto.daily().time().get(i))
                        .temperatureMax(dto.daily().temperature2mMax().get(i))
                        .temperatureMin(dto.daily().temperature2mMin().get(i))
                        .weatherCondition(weatherConditionService.getByCode(dto.daily().weatherCode().get(i)
                                .shortValue()))
                        .location(locationService.getLocation(dto.latitude(), dto.longitude()))
                        .build())
                .collect(Collectors.toList());
    }
}
