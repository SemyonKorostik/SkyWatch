package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationNameRepository extends CrudRepository<LocationName, LocationNameId> {

}
