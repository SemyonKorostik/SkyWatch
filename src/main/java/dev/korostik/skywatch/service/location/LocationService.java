package dev.korostik.skywatch.service.location;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.location.Location;
import java.util.Optional;

public interface LocationService {

  Optional<Location> getNearest(Double latitude, Double longitude);

  Location map(GeoNameDto dto);

  Location save(Location location);
}
