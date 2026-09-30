package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.Location;
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
      ORDER BY l.geog <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography ASC
      LIMIT 1
      """)
  Optional<Location> findNearestByLatitudeAndLongitude(Double latitude, Double longitude,
      Integer radius);

  /*
   * geom geometry(Point, 4326)
   * ST_SetSRID(ST_MakePoint(37.6173, 55.7558), 4326)
   * */
}