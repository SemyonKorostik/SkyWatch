package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.reference.WeatherCondition;
import dev.korostik.skywatch.entities.reference.WeatherProvider;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherConditionRepository extends ListCrudRepository<WeatherCondition, Long> {

  Optional<WeatherCondition> findByProviderNameAndProviderCode(
      @NotNull WeatherProvider providerName, @Size(max = 10) @NotNull String providerCode);

  List<WeatherCondition> findAllByProviderName(@NotNull WeatherProvider providerName);
}
