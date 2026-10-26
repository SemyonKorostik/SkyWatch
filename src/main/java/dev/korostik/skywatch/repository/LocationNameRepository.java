package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.location.LocationName;
import dev.korostik.skywatch.entities.location.LocationNameId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationNameRepository extends CrudRepository<LocationName, LocationNameId> {

  List<LocationName> findByLocationId(Long locationId);

  @Query("select n from LocationName n left join fetch n.location where n.id = :id")
  Optional<LocationName> findWithLocationById(LocationNameId id);
}
