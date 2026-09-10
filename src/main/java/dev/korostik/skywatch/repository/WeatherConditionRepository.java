package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.WeatherCondition;
import dev.korostik.skywatch.entity.WeatherProvider;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherConditionRepository extends CrudRepository<WeatherCondition, Long> {
    Optional<WeatherCondition> findByProviderNameAndProviderCode(@NotNull WeatherProvider providerName, @Size(max = 10) @NotNull String providerCode);
}
