package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.DailyWeather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends CrudRepository<DailyWeather, Long> {
}
