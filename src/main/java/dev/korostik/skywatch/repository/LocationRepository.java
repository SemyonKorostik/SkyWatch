package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.Location;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

  @NativeQuery(value = """
      SELECT *
      FROM locations l
      WHERE ST_DistanceSphere(l.geom, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) <= :radius
      ORDER BY l.geom <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326) ASC
      LIMIT 1
      """)
  Optional<Location> findNearestByLatitudeAndLongitude(Double latitude, Double longitude, Integer radius);

  /*
   * geom geometry(Point, 4326)
   * ST_SetSRID(ST_MakePoint(37.6173, 55.7558), 4326)
   * */
}