package dev.korostik.skywatch.service.location;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import java.util.Optional;

public interface LocationNameService {

  Optional<LocationName> findById(LocationNameId id);

  LocationName mapToEntity(GeoNameDto geoNameDto, Location location, Language language);

  LocationName save(LocationName locationName);
}
