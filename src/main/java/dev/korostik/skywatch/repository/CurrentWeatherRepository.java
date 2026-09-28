package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.CurrentWeather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentWeatherRepository extends CrudRepository<CurrentWeather, Long> {

}
