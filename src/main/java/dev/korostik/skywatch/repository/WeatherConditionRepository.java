package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.WeatherCondition;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherConditionRepository extends CrudRepository<WeatherCondition, Long> {
    Optional<WeatherCondition> findByCode(@NotNull Short code);
}
