package dev.korostik.skywatch.mapper.struct;

import dev.korostik.skywatch.dto.location.LocationNameDto;
import dev.korostik.skywatch.dto.weather.external.response.CurrentWeatherDto;
import dev.korostik.skywatch.dto.weather.external.response.DailyForecastDto;
import dev.korostik.skywatch.dto.weather.external.response.HourlyForecastDto;
import dev.korostik.skywatch.dto.weather.external.response.WeatherConditionDto;
import dev.korostik.skywatch.dto.weather.external.response.WeatherSummaryDto;
import dev.korostik.skywatch.dto.weather.external.response.WmoWeatherCodeDto;
import dev.korostik.skywatch.dto.weather.domain.WeatherSummary;
import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WmoWeatherCode;
import dev.korostik.skywatch.entities.weather.CurrentWeather;
import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeather;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WeatherSummaryMapper {

  WeatherSummaryMapper INSTANCE = Mappers.getMapper(WeatherSummaryMapper.class);

  @Mapping(target = "current", source = "currentWeather")
  @Mapping(target = "daily", source = "dailyWeather")
  @Mapping(target = "hourly", source = "hourlyWeather")
  @Mapping(target = "locationName", source = "locationName")
  WeatherSummaryDto toDto(WeatherSummary summary);

  @Mapping(target = "weatherConditionDto", source = "weatherCondition")
  @Mapping(target = "temperature", source = "temperature")
  @Mapping(target = "windSpeed", source = "windSpeed")
  @Mapping(target = "windDirection", source = "windDirection")
  @Mapping(target = "humidity", source = "humidity")
  @Mapping(target = "pressure", source = "pressure")
  @Mapping(target = "updatedAt", source = "updatedAt")
  CurrentWeatherDto toDto(CurrentWeather entity);

  @Mapping(target = "date", source = "id.date")
  @Mapping(target = "weatherConditionDto", source = "weatherCondition")
  @Mapping(target = "temperatureMax", source = "temperatureMax")
  @Mapping(target = "temperatureMin", source = "temperatureMin")
  @Mapping(target = "windSpeed", source = "windSpeed")
  @Mapping(target = "windDirection", source = "windDirection")
  @Mapping(target = "humidity", source = "humidity")
  DailyForecastDto toDto(DailyWeather entity);

  @Mapping(target = "time", source = "id.time")
  @Mapping(target = "weatherConditionDto", source = "weatherCondition")
  @Mapping(target = "temperature", source = "temperature")
  @Mapping(target = "windSpeed", source = "windSpeed")
  @Mapping(target = "humidity", source = "humidity")
  @Mapping(target = "precipitation", source = "precipitation")
  @Mapping(target = "precipitationProbability", source = "precipitationProbability")
  @Mapping(target = "windDirection", source = "windDirection")
  @Mapping(target = "pressure", source = "pressure")
  HourlyForecastDto toDto(HourlyWeather entity);

  @Mapping(target = "country", source = "country")
  @Mapping(target = "placeName", source = "placeName")
  @Mapping(target = "timeZoneId", source = "location.timeZoneId")
  LocationNameDto toDto(LocationName entity);

  @Mapping(target = "wmoCode", source = "wmoCode")
  @Mapping(target = "temperature", ignore = true)
  @Mapping(target = "windSpeed", ignore = true)
  @Mapping(target = "windDirection", ignore = true)
  @Mapping(target = "humidity", ignore = true)
  @Mapping(target = "pressure", ignore = true)
  WeatherConditionDto toDto(WeatherCondition entity);

  @Mapping(target = "code", source = "code")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "category", source = "category")
  @Mapping(target = "subcategory", source = "subcategory")
  @Mapping(target = "emoji", source = "emoji")
  @Mapping(target = "intensity", source = "intensity")
  @Mapping(target = "weatherGroup", source = "weatherGroup")
  WmoWeatherCodeDto toDto(WmoWeatherCode entity);

  List<DailyForecastDto> toDailyDtoList(List<DailyWeather> entities);
  List<HourlyForecastDto> toHourlyDtoList(List<HourlyWeather> entities);
}
