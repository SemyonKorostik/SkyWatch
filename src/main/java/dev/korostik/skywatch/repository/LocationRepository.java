package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    boolean existsByLatitudeAndLongitude(Float latitude, Float longitude);

    Optional<Location> findByLatitudeAndLongitude(Float latitude, Float longitude);
}