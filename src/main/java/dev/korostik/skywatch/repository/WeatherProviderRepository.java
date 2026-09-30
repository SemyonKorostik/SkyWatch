package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.WeatherProvider;
import dev.korostik.skywatch.enums.ForecastApiProvider;
import jakarta.validation.constraints.Size;
import java.util.List;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface WeatherProviderRepository extends ListCrudRepository<WeatherProvider, String> {
    Optional<WeatherProvider> findByProvider(@Size(max = 255) ForecastApiProvider provider);

    List<WeatherProvider> findAllOrderByPriority(Integer priority);

    List<WeatherProvider> findAllByIsEnabledOrderByPriorityAsc(Boolean isEnabled);

}
