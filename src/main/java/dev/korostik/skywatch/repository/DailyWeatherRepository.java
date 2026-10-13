package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.DailyWeather;
import dev.korostik.skywatch.entities.DailyWeatherId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends ListCrudRepository<DailyWeather, DailyWeatherId> {

}
