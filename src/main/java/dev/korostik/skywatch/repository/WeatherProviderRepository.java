package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.WeatherProvider;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface WeatherProviderRepository extends ListCrudRepository<WeatherProvider, String> {
    Optional<WeatherProvider> findByName(@Size(max = 255) String name);

    List<WeatherProvider> findAllOrderByPriority(Integer priority);

    List<WeatherProvider> findAllByIsEnabledOrderByPriorityAsc(Boolean isEnabled);
}
