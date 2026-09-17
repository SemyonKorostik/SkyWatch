package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.DailyWeather;
import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends ListCrudRepository<DailyWeather, Long> {
}
