package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.weather.DailyWeather;
import dev.korostik.skywatch.entities.weather.DailyWeatherId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends ListCrudRepository<DailyWeather, DailyWeatherId> {

}
