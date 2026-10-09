package dev.korostik.skywatch.service;

import dev.korostik.skywatch.dto.geonames.GeoNameDto;
import dev.korostik.skywatch.entities.Location;
import dev.korostik.skywatch.entities.LocationName;
import dev.korostik.skywatch.entities.LocationNameId;
import dev.korostik.skywatch.enums.Language;
import dev.korostik.skywatch.mapper.LocationNameMapper;
import dev.korostik.skywatch.repository.LocationNameRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationNameService {

  private final LocationNameMapper locationNameMapper;
  private final LocationNameRepository locationNameRepository;

  public Optional<LocationName> findById(LocationNameId id) {
    return locationNameRepository.findById(id);
  }

  public LocationName mapToEntity(GeoNameDto geoNameDto, Location location, Language language) {
    if (location.getId() == null) {
      throw new IllegalStateException("Location must be persisted before mapping name");
    }
    LocationName locationName = locationNameMapper.mapToEntity(geoNameDto);
    locationName.setId(new LocationNameId(location.getId(), language));
    return locationName;
  }

  public LocationName save(LocationName locationName) {
    return locationNameRepository.save(locationName);
  }
}
