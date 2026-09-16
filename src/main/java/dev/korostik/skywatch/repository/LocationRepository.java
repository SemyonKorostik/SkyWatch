package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    boolean existsByLatitudeAndLongitude(Float latitude, Float longitude);

    Optional<Location> findByLatitudeAndLongitude(Double latitude, Double longitude);

    @Query(value = "FROM locations ORDER BY geom <-> ST_SetSRID(ST_MakePoint(:target_lon, :target_lat), 4326) ASC LIMIT 1")
    Optional<Location> findNearestByLatitudeAndLongitude(Float latitude, Float longitude);

    /*
    * geom geometry(Point, 4326)
    * ST_SetSRID(ST_MakePoint(37.6173, 55.7558), 4326)
     * */
}