package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

  @Query(nativeQuery = true, value = """
      SELECT *
      FROM location l
      WHERE ST_DWithin(l.geog, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, :radius)
      ORDER BY l.geog <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
      LIMIT 1
      """)
  Optional<Location> findNearestByLatitudeAndLongitude(double latitude, double longitude,
      int radius);

}