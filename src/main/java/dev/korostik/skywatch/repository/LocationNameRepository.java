package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationNameRepository extends CrudRepository<LocationName, LocationNameId> {

  List<LocationName> findByLocationId(Long locationId);
}
