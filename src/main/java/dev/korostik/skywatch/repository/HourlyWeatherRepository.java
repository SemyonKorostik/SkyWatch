package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.weather.HourlyWeather;
import dev.korostik.skywatch.entities.weather.HourlyWeatherId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyWeatherRepository extends
    ListCrudRepository<HourlyWeather, HourlyWeatherId> {

}
