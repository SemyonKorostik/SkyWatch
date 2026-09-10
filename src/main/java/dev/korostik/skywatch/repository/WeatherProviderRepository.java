package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.WeatherProvider;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WeatherProviderRepository extends CrudRepository<WeatherProvider, String> {
    Optional<WeatherProvider> findByName(@Size(max = 255) String name);
}
