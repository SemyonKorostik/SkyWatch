package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.HourlyWeather;
import dev.korostik.skywatch.entity.HourlyWeatherId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyWeatherRepository extends ListCrudRepository<HourlyWeather, HourlyWeatherId> {

}
